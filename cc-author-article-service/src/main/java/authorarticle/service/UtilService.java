package authorarticle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;

/**
 * TODO write your description here
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */

@Service
public class UtilService {
    Logger logger = LoggerFactory.getLogger(UtilService.class);
    public byte[] getPdfContent(String pdfUrl){
        File file;
        FileInputStream inputStream=null;
        byte[] bytes=null;
        try {
            file = new File(pdfUrl);
            inputStream = new FileInputStream(file);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
        }catch (Exception e){
            logger.info("pdf get error");
        }finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }catch (Exception e){
                logger.info("FileStream close error");
            }
        }
        return bytes;
    }
}