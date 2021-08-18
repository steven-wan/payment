第一步：在你本地或者服务器 Java 安装目录：xxx\jdk1.8\jre\lib\security 找到这个文件 java.security

在 security.provider.1=sun.security.provider.Sun
security.provider.2=sun.security.rsa.SunRsaSign
security.provider.3=sun.security.ec.SunEC
security.provider.4=com.sun.net.ssl.internal.ssl.Provider
security.provider.5=com.sun.crypto.provider.SunJCE
security.provider.6=sun.security.jgss.SunProvider
security.provider.7=com.sun.security.sasl.Provider
security.provider.8=org.jcp.xml.dsig.internal.dom.XMLDSigRI
security.provider.9=sun.security.smartcardio.SunPCSC
security.provider.10=sun.security.mscapi.SunMSCAPI
下面 新增一条信息 ：
security.provider.11=org.bouncycastle.jce.provider.BouncyCastleProvider 

第二步：在你本地或者服务器 Java 安装目录：xxxx\jdk1.8\jre\lib\ext 找对接人给你一个 bcprov-jdk15on-1.62.jar 放入到该目录中。

其他注意事项：
- 如果 pom.xml 文件中的 eclipselink jar 包下载不下来，可以找对接人，暂时用它 maven 的 setting.xml 去下载这个.
- 如果上面解决不了，可以使用 jackson 中的 xml  去处理这个
- 如果上面所有问题都处理了，还是有问题，比如 window 环境出现问题，看下 Java 环境是不是用的 oracle 配套的 java 环境导致的（诸暨环境就是 oracle 环境导致的，Java 环境）

