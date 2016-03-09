package com.jacksen.permissionutil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * permission util
 * Created by jacksen on 2016/3/9.
 */
public class PermissionUtil {


    /**
     * check permission have been granted or not.
     *
     * @param context
     * @param permission
     * @return
     */
    private static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * check some permissions have been granted or not.
     *
     * @param context
     * @param permissions
     * @return
     */
    private static boolean hasPermissions(@NonNull Context context, @NonNull String... permissions) {
        for (String per : permissions) {
            if (!hasPermission(context, per)) {
                return false;
            }
        }
        return true;
    }


    /**
     * request some permissions.
     *
     * @param context
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissions(@NonNull Context context, @NonNull int requestCode,boolean flag,  @NonNull String... permissions) {
        checkObjectSuitability(context);
        if (flag) {
            flag = false;
            for (String per : permissions) {
                flag = flag || shouldShowRequestPermissionRationale(context, per);
            }
        }
        if (flag) {//show the rationale
            runShowRationaleMethod(context, requestCode);
        } else {
            startRequest(context, permissions, requestCode);
        }
    }


    /**
     * invoke android method to request permission
     *
     * @param object
     * @param permissions
     * @param requestCode
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void startRequest(@NonNull Object object, @NonNull String[] permissions, @NonNull int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, permissions, requestCode);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).requestPermissions(permissions, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermissionRationale(@NonNull Object object, @NonNull String permission) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, permission);
        } else if (object instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) object).shouldShowRequestPermissionRationale(permission);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }


    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(@NonNull Context context, @NonNull int requestCode, @NonNull String[] permissions, int[] grantResults) {
        checkObjectSuitability(context);

        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

            } else {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.isEmpty()) {// grant success
            runGrantedMethod(context, requestCode);
        } else {
            runDeniedMethod(context, requestCode);
        }
    }


    /**
     * run the success annotation method
     *
     * @param object
     * @param requestCode
     */
    private static void runGrantedMethod(@NonNull Object object, @NonNull int requestCode) {
        Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(OnPermissionGranted.class)) {
                OnPermissionGranted onGranted = method.getAnnotation(OnPermissionGranted.class);
                int code = onGranted.value();
                if (requestCode == code) {
                    boolean flag = method.isAccessible();
                    method.setAccessible(true);
                    //
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    method.setAccessible(flag);
                }
            }
        }
    }


    /**
     * run the failed annotation method
     *
     * @param object
     * @param requestCode
     */
    private static void runDeniedMethod(@NonNull Object object, @NonNull int requestCode) {
        Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(OnPermissionDenied.class)) {
                OnPermissionDenied onDenied = method.getAnnotation(OnPermissionDenied.class);
                int code = onDenied.value();
                if (requestCode == code) {
                    boolean flag = method.isAccessible();
                    method.setAccessible(true);
                    //
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    method.setAccessible(flag);
                }
            }
        }
    }

    /**
     * run the rationale annotation method
     *
     * @param object
     * @param requestCode
     */
    private static void runShowRationaleMethod(@NonNull Object object, @NonNull int requestCode) {
        Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ShowRationale.class)) {
                ShowRationale showRationale = method.getAnnotation(ShowRationale.class);
                int code = showRationale.value();
                if (requestCode == code) {
                    boolean flag = method.isAccessible();
                    method.setAccessible(true);
                    //
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    method.setAccessible(flag);
                }
            }
        }
    }


    /**
     * @param object
     */
    private static void checkObjectSuitability(@NonNull Object object) {
        if (!((object instanceof Activity) || (object instanceof android.support.v4.app.Fragment) || (object instanceof Fragment))) {
            throw new IllegalArgumentException("the caller must be a fragment or activity!");
        }
    }

}
