package ${package}.dao;

import java.util.List;

import ${package}.domain.MetaSchemaDomain;

#if($framework.contains('mybatis'))
import org.apache.ibatis.annotations.Mapper;
#end

#if($framework.contains('mybatis'))
@Mapper
#end
public interface Repository {

    /**
     * persist domain
     * @param domain
     * @return id
     */
    Long save(MetaSchemaDomain domain);

    /**
     * delete one row with id
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * update to new value when match old value
     * @param newValue
     * @param oldValue
     * @return
     */
    int update(MetaSchemaDomain newValue, MetaSchemaDomain oldValue);

    /**
     * find data by primary key or unique key
     * @param condition
     * @return at most one criteria
     */
    MetaSchemaDomain findOne(MetaSchemaDomain condition);

    /**
     * count qualified rows
     * @param condition
     * @return
     */
    int count(MetaSchemaDomain condition);

    /**
     * fetch qualified rows in range
     * @param condition
     * @param offset
     * @param limit
     * @return
     */
    List<MetaSchemaDomain> query(MetaSchemaDomain condition, int offset, int limit);
}