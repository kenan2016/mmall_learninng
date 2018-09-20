package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author kenan
 * @description 商品分类的业务实现层
 * @date 2018/9/18
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {

        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        // 默认这个分类是可以用的
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            ServerResponse.createBySuccessMessage("添加分类成功!");
        }
        return ServerResponse.createByErrorMessage("添加分类失败！");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /***
    * 递归查询本节点的id 和 孩子节点的id
    * @author kenan
    * @date 2018/9/20
    * @param
    * @return com.mmall.common.ServerResponse
    */
    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        // 初始化set
        Set<Category> categorySet = Sets.newHashSet();//这个是guava类库提供的方法
        // 调用递归算法
        // 经过这个算法 categorySet 变量会被重新出来
//      categorySet = findChildCategory(categorySet, categoryId);
        findChildCategory(categorySet, categoryId);
        // 这个Lists 也是guava里的一个方法
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }

        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
    * 递归函数：递归算法就是自己调用自己
    * 说明：使用Set结构可以用来排重，但是请注意,如果Set<xx> 集合里的对象是基本的结构比如int integer时可以很容易排重
     * 包括String 这种对象，排重也很简单，因为String 本身重写了equals方法。
    * 对于复杂对象，我们需要重写一下equals 方法 hashcode()方法
     * 我们认为category 对象的id 相同时，我们就会
    * @author kenan
    * @date 2018/9/19
    * @param
    * @return
    */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category !=null ) {
            // 因为重写了 category这个复杂对象的equals方法和hashcode方法（比较categoryid,如果id相同，就会被当做同一个对象。
            // 当同一对象被放入set集合时，将会被去重。），所以
            categorySet.add(category);
        }
        // 查找子节点，递归算法一定要有一个退出的条件
        // 我们这里的递归算法的退出条件就是：当子节点为空时，跳出递归算法。
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        // 注意mybatis咋查询出一个集合是，即使出来的 集合元素为 0 也不会出现null 的情况。因此我们不要进行空判断，
        // 如果我们是调用一些不可预知的方法。我们就一定要添加空判断！
        for (Category categoryItem : categoryList) {
            // 当forEach遍历到头时，递归算法结束
            // 注意这个时候我们再传categoryId的时候我们就要传当前category的id,这样才能继续遍历其子节点。
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }


}
