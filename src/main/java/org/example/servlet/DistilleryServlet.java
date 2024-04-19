package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.DistilleryDto;
import org.example.dto.DistilleryVineDto;
import org.example.service.impl.DistilleryService;
import org.example.util.ServletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = {"/distillery/*"}, name = "distilleryServlet")
public class DistilleryServlet extends HttpServlet {
    private final DistilleryService distilleryService = new DistilleryService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<DistilleryDto> allDistilleryDto = distilleryService.getAll();
            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, allDistilleryDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            DistilleryDto distilleryDto = distilleryService.getById(id);

            if (distilleryDto.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Distillery");
                return;
            }

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, distilleryDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To add new distillery path should be empty");
        } else {
            BufferedReader br = req.getReader();
            DistilleryVineDto distilleryVineDtoJSON = mapper.readValue(ServletUtil.readJson(br), DistilleryVineDto.class);
            DistilleryDto distilleryDto = distilleryService.save(distilleryVineDtoJSON.getDistillery(), distilleryVineDtoJSON.getVineID());

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, distilleryDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To edit distillery's info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            DistilleryDto distilleryDtoCheck = distilleryService.getById(id);

            if (distilleryDtoCheck.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Distillery");
                return;
            }

            BufferedReader br = req.getReader();
            DistilleryDto distilleryDtoJSON = mapper.readValue(ServletUtil.readJson(br), DistilleryDto.class);
            DistilleryDto distilleryDto = distilleryService.update(id, distilleryDtoJSON);

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, distilleryDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To delete distillery provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            DistilleryDto distilleryDtoCheck = distilleryService.getById(id);

            if (distilleryDtoCheck.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Distillery");
                return;
            }

            boolean isDeleted = distilleryService.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Distillery with id = %d delete status = %b", id, isDeleted)
            );

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }

}
