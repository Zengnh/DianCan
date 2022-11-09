package com.king.base.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.ViewDataBinding;

import com.king.base.R;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public
class ViewDataBindingUtils {

    public static ViewDataBinding getViewBinding(Class<?> clazz, LayoutInflater inflater,int index) {
        Class<?> aClass = getParamClass(clazz, index);
        return getViewBinding(aClass,inflater);
    }

    public static Class<?> getParamClass(Class<?> clazz, int index) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            return (Class<?>) types[index];
        }
      return null;
    }

    public static ViewDataBinding getViewBinding(Class<?> clazz,LayoutInflater inflater){
        if (clazz != null){
            try {
                Method method = clazz.getDeclaredMethod("inflate", LayoutInflater.class);
                ViewDataBinding  binding = (ViewDataBinding) method.invoke(null, inflater);
                return binding;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ViewDataBinding getViewBinding(Class<?> clazz, LayoutInflater inflater, ViewGroup viewGroup,boolean attachToRoot){
        if (clazz != null){
            try {
                Method method = clazz.getDeclaredMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);
                ViewDataBinding  binding = (ViewDataBinding) method.invoke(null, inflater,viewGroup,attachToRoot);
                return binding;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int getInt(int index,int ... arr){
        return arr[index];
    }
}
