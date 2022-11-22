package com.geek.nacos.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.geek.microservice.service1.api.IService1Api;
import com.geek.microservice.service2.api.IService2Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author geek
 */
@RestController
public class RestConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 通过负载均衡发现地址，流程是从服务发现中心拿 nacos-restful-provider 服务的列表，通过负载均衡算法获取一个地址。（客户端负载均衡，底层 Ribbon）。
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * Dubbo。
     */
    @Reference
    private IService2Api service2Api;

    /**
     * Dubbo。
     */
    @Reference
    private IService1Api service1Api;

    @Value("${provider.address}")
    private String provider;

    @Value("${common.name}")
    private String commonName;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    /**
     * original。
     *
     * @return
     */
    @GetMapping("/service")
    public String service() {
        // 远程调用。
        String object = this.restTemplate.getForObject("http://" + provider + "/service", String.class);
        return " ~ RestConsumerController ~ /service ~ " + object;
    }

    /**
     * nacos 服务发现 ~ 指定服务名。
     *
     * @return
     */
    @GetMapping("/service/nacos/loadBalanceClient")
    public String service1() {
        // 指定服务名。
        String serviceId = "nacos-restful-provider";
        // 发现一个地址。
        ServiceInstance serviceInstance = this.loadBalancerClient.choose(serviceId);
        System.out.println(JSONObject.toJSONString(this.loadBalancerClient));
        // 获取一个 http:// 开头的地址。ip + 端口。
        URI uri = serviceInstance.getUri();
        System.out.println("uri = " + uri);
        // uri = http://192.168.31.159:56010
        String object = restTemplate.getForObject(uri + "/service", String.class);
        return " ~ RestConsumerController ~ /service1 ~ " + object;
    }

    /**
     * Dubbo。
     *
     * @return
     */
    @GetMapping("/dubbo/invoke2")
    public String dubboInvoke2() {
        // rpc 调用 Service2。
        String s = this.service2Api.dubboService2();
        return " ~ RestConsumerController ~ /dubbo/invoke2 ~ " + s;
    }

    /**
     * Dubbo。
     *
     * @return
     */
    @GetMapping("/dubbo/invoke1")
    public String dubboInvoke1() {
        // rpc 调用 Service1。
        String s = this.service1Api.dubboService1();
        return " ~ RestConsumerController ~ /dubbo/invoke1 ~ " + s;
    }

    @RequestMapping("/config/value")
    public String getValue() {
        return commonName;
    }

    @RequestMapping("/config/configurableApplicationContext")
    public String configurableApplicationContext() {
//        return commonName;
        // 实现获取更新配置。要用 bootstrap.yml。
        return this.configurableApplicationContext.getEnvironment().getProperty("common.name");
    }

    @RequestMapping("/configs")
    public String configs() {
        String name = this.configurableApplicationContext.getEnvironment().getProperty("common.name");
        String address = this.configurableApplicationContext.getEnvironment().getProperty("common.address");
        return name + " ~ " + address;
    }

}
