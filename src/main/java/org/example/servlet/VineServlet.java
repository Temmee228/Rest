package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.VineDto;
import org.example.service.impl.VineService;
import org.example.util.ServletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/vine/*"}, name = "vineServlet")
public class VineServlet extends HttpServlet {
    private final VineService vineService = new VineService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<VineDto> allVineDto = vineService.getAll();
            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, allVineDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            VineDto vineDto = vineService.getById(id);

            if (vineDto.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Vine");
                return;
            }

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, vineDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To add new vine path should be empty");
        } else {
            BufferedReader br = req.getReader();
            VineDto vineDtoJSON = mapper.readValue(ServletUtil.readJson(br), VineDto.class);
            VineDto vineDto = vineService.save(vineDtoJSON);

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, vineDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To edit vine's info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            VineDto vineDtoCheck = vineService.getById(id);

            if (vineDtoCheck.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Vine");
                return;
            }

            BufferedReader br = req.getReader();
            VineDto vineDtoJSON = mapper.readValue(ServletUtil.readJson(br), VineDto.class);
            VineDto vineDto = vineService.update(id, vineDtoJSON);

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, vineDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To delete vine provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            VineDto vineDtoCheck = vineService.getById(id);

            if (vineDtoCheck.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Vine");
                return;
            }

            boolean isDeleted = vineService.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Vinewith id = %d delete status = %b", id, isDeleted)
            );

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }
}
