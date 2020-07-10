package com.johan.router;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by johan on 2020/7/6.
 */

public class AppRouter {

    private static AppRouter instance;
    private static boolean hasInit;

    public static synchronized void init(Context context) {
        if (hasInit) return;
        hasInit = true;
        instance = new AppRouter();
        Set<String> routerBuilderSet = RouterHelper.loadRouterBuilderSet(context);
        try {
            for (String routerBuilder : routerBuilderSet) {
                ((RouterBuilder) (Class.forName(routerBuilder).newInstance())).build(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void bind(Activity activity, Bundle savedInstanceState) {
        RouterHelper.bind(activity, activity.getIntent(), savedInstanceState);
    }

    public static void save(Activity activity, Bundle outState) {
        RouterHelper.save(activity, outState);
    }

    public static RouterIntent with(Context context) {
        return new RouterIntent(context, instance);
    }

    private Map<String, Class> routerMap;

    private AppRouter() {
        routerMap = new HashMap<>();
    }

    public void putRoute(String path, Class target) {
        routerMap.put(path, target);
    }

    public Class getRoute(String path) {
        return routerMap.get(path);
    }

}
