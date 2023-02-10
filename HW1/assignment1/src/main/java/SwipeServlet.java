import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SwipeServlet", value = "/SwipeServlet")
public class SwipeServlet extends HttpServlet {
  private Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      res.setContentType("text/plain");
      String urlPath = req.getPathInfo();

      // check we have a URL!
      if (urlPath == null || urlPath.isEmpty()) {
          res.setStatus(HttpServletResponse.SC_NOT_FOUND);
          res.getWriter().write("missing paramterers");
          return;
      }

      String[] urlParts = urlPath.split("/");
      // and now validate url path and return the response status code
      // (and maybe also some value if input is valid)

      if (!isUrlValid(urlParts)) {
          res.setStatus(HttpServletResponse.SC_NOT_FOUND);
          res.getWriter().write("Invalid URL");
      } else {
          res.setStatus(HttpServletResponse.SC_OK);
          // do any sophisticated processing with urlParts which contains all the url params
          // TODO: process url params in `urlParts`
          res.getWriter().write("It works!");
      }
  }
  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/left" or "/right"
    if(urlPath == null || urlPath.length == 0 || urlPath.length != 2){
      return false;
    }

    if(!urlPath[1].isEmpty()){
      if(!"left".equalsIgnoreCase(urlPath[1]) && !"right".equalsIgnoreCase(urlPath[1])){
        return false;
      }
      return true;
    }

    return true;
  }
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("Invalid URL");
    } else {
      response.setContentType("application/json");
//      Gson gson = new Gson();

      try{
        StringBuilder sb = new StringBuilder();
        String s;
        while((s = request.getReader().readLine()) != null){
          sb.append(s);
        }
        DatingContend contend = gson.fromJson(sb.toString(),DatingContend.class);
        if(contend.ifValidRequest()){
          response.setStatus(HttpServletResponse.SC_CREATED);
          response.getWriter().write(gson.toJson("Post successfully"));

        }else{
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.getWriter().write(gson.toJson("Invalid post!"));
        }
      }catch(Exception e){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
  }
}
