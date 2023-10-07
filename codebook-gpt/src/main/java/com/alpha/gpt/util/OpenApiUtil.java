package com.alpha.gpt.util;

import com.alpha.gpt.config.OpenApiConfig;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wds
 * @DateTime: 2023/10/3 14:59
 */
@Component
@Slf4j
public class OpenApiUtil {

    private final static String chatModel = "gpt-3.5-turbo";

    @Resource
    OpenApiConfig openApiConfig;

    public OpenAiService service ;
    public static  OpenApiUtil staticOenApiUtil;
    static {
        staticOenApiUtil = new OpenApiUtil();
        staticOenApiUtil.service = init();
    }

    public static void main(String[] args) {
        String prompt ="Somebody once told me the world is gonna roll me";
        staticOenApiUtil.completion(prompt);

        String imagePrompt = "A cow breakdancing with a turtle";
        staticOenApiUtil.image(imagePrompt);

        String chatContent = "You are a dog and will speak as such.";
        staticOenApiUtil.chat(chatContent);


    }
    public static OpenAiService init(){
        String token = "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";
        return new OpenAiService(token, Duration.ofSeconds(30));
//        return new OpenAiService(openApiConfig.getOpenApiToken(), Duration.ofSeconds(30));
    }


    /**
     * 请求
     *
     * @return
     */
    public List<CompletionChoice> completion(String prompt){

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt(prompt)
                .echo(true)
                .user("testing")
                .n(3)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        return choices;

    }
    /**
     * 图片请求
     *
     * @param type 返回图片数据类型 1-url 2-base64
     *
     * @return
     */
    public List<String> image(String prompt,Integer type ){

       List<Image> images = image(prompt);
        return type.equals(1)?
                images.stream().map(Image::getUrl).collect(Collectors.toList()):
                images.stream().map(Image::getB64Json).collect(Collectors.toList());
    }

    /**
     * 图片请求
     *
     * @return
     */
    public List<Image> image(String prompt ){
        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt)
                .build();
        System.out.println("\nImage is located at:");
        return service.createImage(request).getData();
    }

    public void chat(String chatContent){
        System.out.println("Streaming chat completion...");
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), chatContent);
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(chatModel)
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();
        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(System.out::println);
        service.shutdownExecutor();
    }


}