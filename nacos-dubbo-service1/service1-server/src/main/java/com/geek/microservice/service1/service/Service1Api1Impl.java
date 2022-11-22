package com.geek.microservice.service1.service;

import com.geek.microservice.service1.api.IService1Api;
import com.geek.microservice.service2.api.IService2Api;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author geek
 */
@Service
public class Service1Api1Impl implements IService1Api {

    @Reference
    private IService2Api service2Api;

    /**
     * dubbo。
     *
     * @return
     */
    @Override
    public String dubboService1() {
        // 远程调用。
        String s = this.service2Api.dubboService2();
        return " ~ Service1Api1Impl ~ | " + s;
    }

}
