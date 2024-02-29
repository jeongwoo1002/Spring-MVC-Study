package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
       controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
       controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
       controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paraMap

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model =new HashMap<>();//추가
        
        String viewName = controller.process(paramMap, model);

        //new-form
        //viewForm을 가지고 viewResolver를 호출 그리고 Myview를 반환
        MyView view = viewResolver(viewName); //

        // 이걸 가지고 렌더를 호출을 함, 호출하면서 모델을 넘김
        //왜? 뷰가 렌더링이 되려면 모델이 필요해서
        view.render(model, request, response);
    }

    private static MyView viewResolver(String viewName) {
        MyView view = new MyView("/WEB-INF/views/" + viewName + ".jsp"); //Myview를 만들면서 물리이름을 Myview라는 객체가 곧 반환이됨
        return view;
    }

    //parameterMap이라는 걸 만들어서 반환을 해줌
    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
