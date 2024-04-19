package com.hgy.thirdparty;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Autowired
    OSS ossClient;

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
        String objectName = "tu5.jpg";
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

}
