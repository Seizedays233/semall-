 # semall商城项目



**技术栈**： SpringBoot 、SpringCloud-Alibaba、 ThyemLeaf、 Vue、Elastic Search

**数据库**：MySQL、Redis

**功能**：用户认证 社交登录 搜索服务 商品展示 购物车功能 订单提交 支付服务等

**端口**

前端npm 8888

 semall_passport_web 9001 用户认证中心前台

 semall_user_service 8001 用户服务中心后台

 semall_manage_web 9002 后台管理系统 web层

 semall_manage_service 8002 后台管理系统 service层

 semall_search_serivce 8022 搜索服务后台

 semall_search_web 9022 搜索服务前台

 semall_item_web 9012 前端的商品详情展示

 semall_cart_service 8032 购物车服务后台

 semall_cart_web 9032 购物车服务前台

 semall_order_web 9042 订单服务前台

 semall_order_service 8042 订单服务后台

 semall_payment 8087  支付服务



使用前请确保相关软件已安装好 并根据实际修改配置文件

前端ui以及支付宝接口密钥源自网络 根据自己实际进行修改



**2020.6.2 更新**

​	 新增message queue功能 用以完善支付模块以及订单模块

