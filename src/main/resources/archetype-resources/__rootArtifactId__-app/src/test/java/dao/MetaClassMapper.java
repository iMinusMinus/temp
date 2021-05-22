package ${package}.dao;

import java.util.List;

import ${package}.domain.MetaClass;

#if($framework.contains('mybatis'))
import org.apache.ibatis.annotations.Mapper;
#if($configType.contains("@java"))
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
#end
#end

#if($framework.contains('mybatis'))
@Mapper
#end
public interface MetaClassMapper {

    /**
     * persist domain
     * @param domain
     * @return id
     */
#if($configType.contains("@java") and $framework.contains('mybatis'))
    @Insert("INSERT INTO meta_class (class_name, super_class_name) VALUES (#{name}, #{superName})")
#if($framework.contains("pg"))
    @SelectKey(statement = "SELECT currval('meta_class_id_seq')", before = false, keyProperty = "id", resultType = long.class)
#elseif($framework.contains("oracle"))
    @SelectKey(statement = "SELECT META_CLASS_ID_SEQ.nextval from dual", before = true, keyProperty = "id", resultType = long.class)
#end
#end
    long save(MetaClass domain);

    /**
     * delete one row with id
     * @param id
     * @return
     */
#if($configType.contains("@java") and $framework.contains('mybatis'))
    @Delete("DELETE FROM meta_class WHERE id = #{id}")
#end
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
#if($configType.contains("@java") and $framework.contains('mybatis'))
    @Select("<script>SELECT id,class_name,super_class_name FROM meta_class <where><if test='name != null'>class_name = #{name}</if><if test='superName != null'>AND super_class_name=#{superName}</if></where> </script>")
    @Results({@Result(column="id",property="id"),@Result(column="class_name",property="name"),@Result(column="super_class_name", property="superName")})
#end
    MetaClass findOne(MetaClass condition);

    /**
     * count qualified rows
     * @param condition
     * @return
     */
#if($configType.contains("@java") and $framework.contains('mybatis'))
    @Select("<script>SELECT COUNT(*) FROM meta_class <where><if test='name != null'>class_name = #{name}</if><if test='superName != null'>AND super_class_name=#{superName}</if></where> </script>")
#end
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