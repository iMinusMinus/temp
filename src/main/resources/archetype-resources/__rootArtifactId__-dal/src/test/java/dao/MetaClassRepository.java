package ${package}.dao;

import java.util.List;

import ${package}.domain.MetaClass;

#if($framework.contains('mybatis'))
import org.apache.ibatis.annotations.Mapper;
#end

#if($framework.contains('mybatis'))
@Mapper
#end
public interface MetaClassRepository {

    /**
     * persist domain
     * @param domain
     * @return id
     */
    long save(MetaClass domain);

    /**
     * delete one row with id
     * @param id
     * @return
     */
    int delete(long id);

    /**
     * update to new value when match old value
     * @param newValue
     * @param oldValue
     * @return
     */
    int update(MetaClass newValue, MetaClass oldValue);

    /**
     * find data by primary key or unique key
     * @param condition
     * @return at most one criteria
     */
    MetaClass findOne(MetaClass condition);

    /**
     * count qualified rows
     * @param condition
     * @return
     */
    int count(MetaClass condition);

    /**
     * fetch qualified rows in range
     * @param condition
     * @param offset
     * @param limit
     * @return
     */
    List<MetaClass> query(MetaClass condition, int offset, int limit);
}