package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.StoreDto;
import org.example.service.impl.StoreService;
import org.example.util.ServletUtil;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/store/*"}, name = "storeServlet")
public class StoreServlet extends HttpServlet {
    private final StoreService storeService = new StoreService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<StoreDto> allStoreDto = storeService.getAll();
            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, allStoreDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            StoreDto storeDto = storeService.getById(id);

            if (storeDto.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Store");
                return;
            }

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, storeDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To add new store path should be empty");
        } else {
            BufferedReader br = req.getReader();
            StoreDto storeDtoJSON = mapper.readValue(ServletUtil.readJson(br), StoreDto.class);
            StoreDto storeDto = storeService.save(storeDtoJSON);

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, storeDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To edit store's info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            StoreDto storeDtoCheck = storeService.getById(id);

            if (storeDtoCheck.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Store");
                return;
            }

            BufferedReader br = req.getReader();
            StoreDto storeDtoJSON = mapper.readValue(ServletUtil.readJson(br), StoreDto.class);
            StoreDto storeDto = storeService.update(id, storeDtoJSON);

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, storeDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtil.sendBadRequest(resp, "Wrong path. To delete store provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            StoreDto storeDtoCheck = storeService.getById(id);

            if (storeDtoCheck.getId() == 0) {
                ServletUtil.sendNotFound(resp, id, "Store");
                return;
            }

            boolean isDeleted = storeService.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Store with id = %d delete status = %b", id, isDeleted)
            );

            ServletUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }
}
