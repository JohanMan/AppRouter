package com.johan.router;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by johan on 2020/7/8.
 */

public class RouterHelper {

    private static final String KEY_SP = "com.johan.router.shared.preferences";
    private static final String KEY_VERSION = "com.johan.router.version";
    private static final String KEY_ROUTER_BUILDER_SET = "com.johan.router.builder.set";

    /**
     * 加载 ActivityRouterBuilder 类
     * @param context
     * @return
     */
    public static Set<String> loadRouterBuilderSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE);
        Set<String> routerBuilderSet;
        if (isNewVersion(context)) {
            routerBuilderSet = scanRouterBuilder(context);
            sharedPreferences.edit().putStringSet(KEY_ROUTER_BUILDER_SET, routerBuilderSet).commit();
        } else {
            routerBuilderSet = sharedPreferences.getStringSet(KEY_ROUTER_BUILDER_SET, new HashSet<String>());
        }
        return routerBuilderSet;
    }

    /**
     * 扫描 ActivityRouterBuilder 类
     * @param context
     * @return
     */
    private static Set<String> scanRouterBuilder(Context context) {
        Set<String> routerMapSet = new HashSet<>();
        try {
            PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
            DexFile dexFile = new DexFile(context.getPackageResourcePath());
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement();
                Class<?> entryClass = Class.forName(entryName, false, classLoader);
                if (entryClass != RouterBuilder.class && RouterBuilder.class.isAssignableFrom(entryClass)) {
                    routerMapSet.add(entryClass.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routerMapSet;
    }


    /**
     * 是否是新版本
     * @param context
     * @return
     */
    private static boolean isNewVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE);
        int oldVersion = sharedPreferences.getInt(KEY_VERSION, 0);
        int currentVersion = getPackageCode(context);
        System.err.println("router ; " + oldVersion + " === " + currentVersion);
        if (oldVersion == currentVersion) {
            return false;
        }
        sharedPreferences.edit().putInt(KEY_VERSION, currentVersion).apply();
        return true;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    private static int getPackageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 绑定字段
     * @param target
     * @param intent
     * @param savedInstanceState
     */
    public static void bind(Object target, Intent intent, Bundle savedInstanceState) {
        Class targetClass = target.getClass();
        Field[]  fields =  targetClass.getDeclaredFields();
        ValueProvider valueProvider = null;
        if (savedInstanceState != null) {
            valueProvider = new BundleValueProvider(savedInstanceState);
        } else if (intent != null){
            valueProvider = new IntentValueProvider(intent);
        }
        if (valueProvider == null) return;
        for (Field field : fields) {
            if (!field.isAnnotationPresent(BindField.class)) continue;
            try {
                String key = field.getName();
                Class type = field.getType();
                Object value = valueProvider.getValue(key, type);
                if (value == null) continue;
                field.setAccessible(true);
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存字段
     * @param target
     * @param outState
     */
    public static void save(Object target, Bundle outState) {
        Class targetClass = target.getClass();
        Field[]  fields =  targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(BindField.class)) continue;
            try {
                field.setAccessible(true);
                String key = field.getName();
                Class type = field.getType();
                Object value = field.get(target);
                putValue(key, value, type, outState);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void putValue(String key, Object value, Class type, Bundle outState) {
        if (type == int.class || type == Integer.class) {
            outState.putInt(key, (int) value);
        } else if (type == String.class) {
            outState.putString(key, (String) value);
        } else if (type == float.class || type == Float.class) {
            outState.putFloat(key, (float) value);
        } else if (type == boolean.class || type == Boolean.class) {
            outState.putBoolean(key, (boolean) value);
        } else if (Parcelable.class.isAssignableFrom(type)) {
            outState.putParcelable(key, (Parcelable) value);
        } else if (type == long.class || type == Long.class) {
            outState.putLong(key, (long) value);
        } else if (type == double.class || type == Double.class) {
            outState.putDouble(key, (double) value);
        } else if (type == Parcelable.class) {
            outState.putParcelable(key, (Parcelable) value);
        } else {
            throw new RuntimeException("putValue not support " + type + " !!!");
        }
    }

    /**
     * 值提供器
     */
    private interface ValueProvider {
        Object getValue(String key, Class type);
    }

    /**
     * Intent 值提供器
     */
    private static class IntentValueProvider implements ValueProvider {
        private Intent intent;
        public IntentValueProvider(Intent intent) {
            this.intent = intent;
        }
        @Override
        public Object getValue(String key, Class type) {
            if (type == int.class || type == Integer.class) {
                return intent.getIntExtra(key, 0);
            } else if (type == String.class) {
                return intent.getStringExtra(key);
            } else if (type == float.class || type == Float.class) {
                return intent.getFloatExtra(key, 0);
            } else if (type == boolean.class || type == Boolean.class) {
                return intent.getBooleanExtra(key, false);
            } else if (Parcelable.class.isAssignableFrom(type)) {
                return intent.getParcelableExtra(key);
            } else if (type == long.class || type == Long.class) {
                return intent.getLongExtra(key, 0);
            } else if (type == double.class || type == Double.class) {
                return intent.getDoubleExtra(key, 0);
            } else if (type == Parcelable.class) {
                return intent.getParcelableExtra(key);
            }
            throw new RuntimeException("getValueFromIntent not support " + type + " !!!");
        }
    }

    /**
     * Bundle 值提供器
     */
    private static class BundleValueProvider implements ValueProvider {
        private Bundle savedInstanceState;
        public BundleValueProvider(Bundle savedInstanceState) {
            this.savedInstanceState = savedInstanceState;
        }
        @Override
        public Object getValue(String key, Class type) {
            if (type == int.class || type == Integer.class) {
                return savedInstanceState.getInt(key, 0);
            } else if (type == String.class) {
                return savedInstanceState.getString(key);
            } else if (type == float.class || type == Float.class) {
                return savedInstanceState.getFloat(key, 0);
            } else if (type == boolean.class || type == Boolean.class) {
                return savedInstanceState.getBoolean(key, false);
            } else if (Parcelable.class.isAssignableFrom(type)) {
                return savedInstanceState.getParcelable(key);
            } else if (type == long.class || type == Long.class) {
                return savedInstanceState.getLong(key, 0);
            } else if (type == double.class || type == Double.class) {
                return savedInstanceState.getDouble(key, 0);
            } else if (type == Parcelable.class) {
                return savedInstanceState.getParcelable(key);
            }
            throw new RuntimeException("getValueFromBundle not support " + type + " !!!");
        }
    }

}
