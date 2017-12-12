/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rafael
 */
@RestController
public class ClientController {
    @GetMapping("/api/client/server")
    public ResponseEntity<Void> getServer(HttpServletRequest request){
        if (!NetworkApp.address.contains(request.getRemoteAddr())){
            NetworkApp.address.add(request.getRemoteAddr());
            NetworkApp.updateServerList();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
