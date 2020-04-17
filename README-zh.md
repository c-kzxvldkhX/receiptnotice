### 提示
- `很感谢你的使用`
- `本软件是开源不盈利软件，没有所谓的定制版，不存在任何的收费QQ群，望周知`



#### 软件规范及适用范围

- 该软件的适用范围只限于帮助个人用户获取、收集自己的收款信息，它等效于您手工的获取过程。
- 如您没有上述手工获取的权限，您将无法使用该软件。
- 不能将本项目用于商业用途。



#### 应用工作原理

- 手机安装一个app,然后这个服务监听手机收到的通知，如果是想要获取的通知，就把信息推送到指定的url去。

#### 本软件使用方法

- 安装后先将软件加入系统白名单，各个安卓系统的方法各有不同

- 打开软件自动跳转到获取通知权限页面，允许本应用监控通知

- 返回到软件主页，填写你要接受收款信息通知的url,软件在接到收款通知后，会用post的方法，发送json信息.

- 详细使用方法，参考[wiki](https://github.com/WeihuaGu/receiptnotice/wiki)

#### 这个可搭配服务端项目

|getreceipt-server |
|:-|
|[getreceipt-server](https://github.com/WeihuaGu/getreceipt-server)|

##### 捐助
[![](https://img.shields.io/badge/%E6%8D%90%E5%8A%A9-%E6%94%AF%E4%BB%98%E5%AE%9D%7C%E5%BE%AE%E4%BF%A1%7C%E4%BA%91%E9%97%AA%E4%BB%98%7CPayPal-green.svg)](https://weihuagu.github.io/donate)

##### 引用项目
| ||
|-|-|
|本软件从NLservice修改而来| [NLservice](https://github.com/WHD597312/NLservice)|
|实时logcat | [Lynx](https://github.com/pedrovgs/Lynx) |

##### 开源许可
本项目的代码，wiki等资源基于一个修改版的apache2.0 license协议发布，这意味着你可以拷贝、再发行本项目的内容, 但是你将必须：

不能将本项目用于商业用途，任何盈利活动都属于商业用途。
在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议，商标，专利声明和其他原来作者规定需要包含的说明。


