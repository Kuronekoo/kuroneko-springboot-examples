package cn.kuroneko.demos.selector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 代理选择器
 *
 * @author liwei
 * @date 2019/12/4 11:04 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class KuronekoHttpProxySelector extends ProxySelector {

    private List<Proxy> proxies;
    private Collection<String> nonProxyHosts;

    @Override
    public List<Proxy> select(URI uri) {
        if (CollectionUtils.isEmpty(nonProxyHosts)) {
            throw new IllegalStateException(
                    "There is no proxy to deal with, please make sure VwopHttpProxySelector configured correctly before requesting.");
        }
        String host = uri.getHost();
        //匹配是否为非代理地址
        for (String nonProxyHost : nonProxyHosts) {
            if (PatternMatchUtils.simpleMatch(nonProxyHost, host)) {
                return Arrays.asList(Proxy.NO_PROXY);
            }
        }
        return proxies;
    }

    public void addProxy(Proxy proxy) {

    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        //只有一个proxy, 在此不做List<Proxy>重排
        if (CollectionUtils.isEmpty(proxies)) {
            throw new IllegalStateException(
                    "There is no proxy to deal with, please make sure VwopHttpProxySelector configured correctly.");
        }
    }
}
