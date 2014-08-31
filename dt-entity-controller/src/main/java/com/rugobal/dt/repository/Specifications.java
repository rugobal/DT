package com.rugobal.dt.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.jpa.domain.Specification;

/**
 * Class that contains common specifications for all entities to be used along
 * with JPA Repositories.
 * 
 * More info on Spring Repositories can be found <a href=
 * "http://static.springsource.org/spring-data/data-jpa/docs/current/reference/html/jpa.repositories.html"
 * > here </a>
 * 
 * @author Ruben Gomez
 * 
 * @param <T>
 */
public final class Specifications<T> {

    private Specifications() {
    }

    /**
     * Generic specification to retrieve entities by column and value.
     * 
     * @param colAttribute
     * @param colValue
     * @return specification to retrieve entities by column and value
     */
    public static <T1, T2> Specification<T1> byColAndValue(final SingularAttribute<T1, T2> colAttribute, final T2 colValue) {

        return new Specification<T1>() {

            public Predicate toPredicate(Root<T1> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final String colName = colAttribute.getName();
                return cb.equal(root.get(colName), colValue);
            }

        };
    }
}
