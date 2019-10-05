package ${package};

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
public interface MetaFieldMapper {

}