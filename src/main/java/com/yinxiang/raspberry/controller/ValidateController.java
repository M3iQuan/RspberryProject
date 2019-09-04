package com.yinxiang.raspberry.controller;


import com.yinxiang.raspberry.validate.code.ImageCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;



@RestController
public class ValidateController {
    @GetMapping("/code/image/**")
    @ResponseBody
    public void code(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ImageCode ic = new ImageCode();
        BufferedImage image = ic.getImage();
        String text = ic.getText();
        System.out.println("text:" + text);
        HttpSession session = req.getSession();
        session.setAttribute("imageCode", text); //!!!!
        System.out.println("req:" + req.toString());
        ImageCode.output(image, resp.getOutputStream());
    }
}
