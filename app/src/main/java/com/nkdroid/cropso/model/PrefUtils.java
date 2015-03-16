/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nkdroid.cropso.model;

import android.content.Context;

import com.nkdroid.cropso.Custom.ComplexPreferences;

public class PrefUtils {


//
//    public static boolean isLoggedIn(Context context) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        return sp.getBoolean("isLogin", false);
//    }
//
//    public static void setLoggedIn(final Context context, final boolean isLoggedIn) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        sp.edit().putBoolean("isLogin", isLoggedIn).commit();
//    }
//
//    public static void clearLogin(Context context) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        sp.edit().remove("isLogin").commit();
//    }
//



    public static User getUser(final Context context){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "user_pref", 0);
        return complexPreferences.getObject("current_user", User.class);
    }


    public static void setUser(final Context context, final User affilateUser) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "user_pref", 0);
        complexPreferences.putObject("current_user", affilateUser);
        complexPreferences.commit();
    }

    public static User getClient(final Context context){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "client_pref", 0);
        return complexPreferences.getObject("current_client", User.class);
    }


    public static void setClient(final Context context, final User affilateUser) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "client_pref", 0);
        complexPreferences.putObject("current_client", affilateUser);
        complexPreferences.commit();
    }

    public static User getManager(final Context context){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "manager_pref", 0);
        return complexPreferences.getObject("current_manager", User.class);
    }


    public static void setManager(final Context context, final User affilateUser) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "manager_pref", 0);
        complexPreferences.putObject("current_manager", affilateUser);
        complexPreferences.commit();
    }
    public static Project getProject(final Context context){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "project_pref", 0);
        return complexPreferences.getObject("current_project", Project.class);
    }


    public static void setProject(final Context context, final Project affilateUser) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "project_pref", 0);
        complexPreferences.putObject("current_project", affilateUser);
        complexPreferences.commit();
    }

    public static User getEmployee(final Context context){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "employee_pref", 0);
        return complexPreferences.getObject("current_employee", User.class);
    }


    public static void setEmployee(final Context context, final User affilateUser) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "employee_pref", 0);
        complexPreferences.putObject("current_employee", affilateUser);
        complexPreferences.commit();
    }

}
