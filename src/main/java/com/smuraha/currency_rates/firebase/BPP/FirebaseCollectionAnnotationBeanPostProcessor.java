package com.smuraha.currency_rates.firebase.BPP;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class FirebaseCollectionAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        FirebaseCollection annotation = beanClass.getAnnotation(FirebaseCollection.class);
        if(annotation!=null){
            String collectionName = annotation.name();
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collectionReference = firestore.collection(collectionName);
            Field refField = ReflectionUtils.findField(beanClass, "collection");
            if (refField != null) {
                ReflectionUtils.setField(refField,bean,collectionReference);
            }
        }
        return bean;
    }
}
