package com.hgy.product;

import java.io.*;

import com.aliyun.oss.*;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hgy.product.entity.BrandEntity;
import com.hgy.product.service.BrandService;
import com.hgy.product.service.CategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OSS ossClient;

    @Test
    public void testFindPath(){
        Long[] paths = categoryService.findCategoryPath(167L);
        log.info("完整路径：{}", Arrays.asList(paths));
    }

    @Test
    public void testUpload() throws ClientException, FileNotFoundException {
//        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
//        // 从环境变量中获取API访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
////        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        String accessKeyId = "LTAI5t7WZ1YbHuzJ76bBJGrn";
//        String accessKeySecret = "aKdPS0888OWuQKhqCGsu5Re71HWRVg";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "gulimall-hgy";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "tu2.jpg";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath = "C:\\Users\\huang'guo'yong\\Pictures\\Saved Pictures\\tu2.jpg";
        //把文件地址变成一个流，方便调用
        FileInputStream inputStream = new FileInputStream(filePath);
        //上传文件
        ossClient.putObject(bucketName, objectName, inputStream);
        //关闭ossClient
        ossClient.shutdown();

        System.out.println("文件上传完成。。。");
    }

    @Test
    public void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();

//        brandEntity.setName("华为"); //添加的字段
//        brandService.save(brandEntity); //保存到数据库

//        brandEntity.setBrandId(1L); //修改那条数据的id
//        brandEntity.setDescript("遥遥领先"); //修改的信息
//        brandService.updateById(brandEntity); //保存修改

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));

        list.forEach((item) -> {
            System.out.println(item);
        });

//        System.out.println("修改成功。。。");
    }

}
