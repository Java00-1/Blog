package com.pdf.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    //no args constructor
    private BeanCopyUtils(){}

    //package single

    /**
     *
     * @param resource resource obj
     * @param clazz clazz obj
     * @param <V> method generic
     * @return
     */
    public static <V> V copyBean(Object resource,Class<V> clazz){
        //target obj
        V result = null;
        try {
            //reflect create obj
            result = clazz.newInstance();
            //copy
            BeanUtils.copyProperties(resource,result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //return
        return result;
    }

    //package list obj
    public static<O,V>  List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(new Function<O, V>() {
                    @Override
                    public V apply(O o) {
                        return copyBean(o, clazz);
                    }
                })
                .collect(Collectors.toList());
    }
}














