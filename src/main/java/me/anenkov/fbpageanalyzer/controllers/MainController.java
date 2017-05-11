package me.anenkov.fbpageanalyzer.controllers;

import me.anenkov.fbpageanalyzer.controllers.util.SocialControllerUtil;
import me.anenkov.fbpageanalyzer.dao.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * MainController
 *
 * @author anenkov
 */
@Controller
public class MainController {

    private final ConnectionRepository connectionRepository;
    private final DataDao dataDao;
    private final SocialControllerUtil util;

    /**
     * @param connectionRepository
     * @param dataDao
     * @param util
     */
    @Autowired
    public MainController(ConnectionRepository connectionRepository, DataDao dataDao, SocialControllerUtil util) {
        this.connectionRepository = connectionRepository;
        this.dataDao = dataDao;
        this.util = util;
    }

    /**
     * @param request
     * @param currentUser
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String home(HttpServletRequest request, Principal currentUser,
                       Model model) {
        util.setModel(request, currentUser, model);
        return "home";
    }

    /**
     * @param request
     * @param currentUser
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Principal currentUser,
                        Model model) {
        util.setModel(request, currentUser, model);
        return "login";
    }

    /**
     * @param request
     * @param currentUser
     * @param model
     * @param data
     * @return
     */
    @RequestMapping(value = "/update", method = POST)
    public String update(
            HttpServletRequest request, Principal currentUser, Model model,
            @RequestParam(value = "data") String data) {

        String userId = currentUser.getName();
        dataDao.setData(userId, data);

        util.setModel(request, currentUser, model);
        return "home";
    }

    /**
     * @param webRequest
     * @param request
     * @param currentUser
     * @param model
     * @param status
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = POST)
    public String updateStatus(
            WebRequest webRequest, HttpServletRequest request, Principal currentUser, Model model,
            @RequestParam(value = "status") String status) {
        MultiValueMap<String, Connection<?>> cmap = connectionRepository.findAllConnections();
        Set<Map.Entry<String, List<Connection<?>>>> entries = cmap.entrySet();
        for (Map.Entry<String, List<Connection<?>>> entry : entries) {
            for (Connection<?> c : entry.getValue()) {
                c.updateStatus(status);
            }
        }

        return "home";
    }
}
