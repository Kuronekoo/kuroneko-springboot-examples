package cn.kuroneko.demos.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MySpecification<T> implements Specification<T> {

    private Class<T> clazz;
    private Map<String,Object> conditon;
    public MySpecification(){

    }
    public MySpecification(Class<T> clazz,Map<String,Object> conditon){
        this.clazz=clazz;
        this.conditon=conditon;
    }
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Field[] fields = clazz.getDeclaredFields();
        List<Predicate> predicates = new ArrayList<>();
        Arrays.stream(fields).forEach(f->{
            f.setAccessible(true);
            String fieldName = f.getName();
            if(conditon.containsKey(fieldName)){
                if(null==conditon.get(fieldName)){
                    return;
                }
                predicates.add(cb.like(root.get(fieldName),String.format("%%%s%%",conditon.get(fieldName))));
            }
        });
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
