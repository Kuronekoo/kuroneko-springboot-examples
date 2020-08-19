package cn.kuroneko.demos.commons.group.service;

import cn.kuroneko.demos.commons.emoji.emoji.EmojiConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 23:45
 **/
@Slf4j
@Component
public class EmojiCommandRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String emoji = EmojiConverter.getInstance().toAlias("-----> get some emojis 😀😃 ...");
        log.info(emoji);
    }
}
