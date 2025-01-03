
package cn.oli.crm.handler;

import cn.oli.crm.exceptions.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Rest Client 异常处理
 * @author lyl
 */
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(@Nonnull ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String body = convertStreamToString(response.getBody());
        try {
            errorHandler.handleError(response);
        } catch (RestClientException scx) {
            log.error("调用失败", scx);
            throw new InternalServerException(body);
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("convertStreamToString:转换response.getBody出现异常", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("convertStreamToString:关闭输入流出现异常", e);
            }
        }
        return sb.toString();
    }
}
