/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

public class ToolbarController {
    
    //  Etusivu Viestit Profiili Signout
    //  Etusivu Viestit Signup   Login
    
    //private String profile = "<span></span>";
    //private String login;
    //private String logout;
    //private String viestit;
    
    public static String parseToolbar(boolean isLoggedIn) {
        return parseToolbar(isLoggedIn, null, null);
    }
    
    public static String parseToolbar(boolean isLoggedIn, String userName, String currentPage) {
        if (!isLoggedIn) {
            return parseLoggedOutToolbar(currentPage);
        } else {
        String front = "<span class=\"toolbar-link\"><a href=\"/\">Front page</a></span>";
        String messages = "<span class=\"toolbar-link\"><a href=\"/viestit\">All messages</a></span>";
        String profile  = "<span class=\"toolbar-link\"><a href=\"/profile\">Profile</a></span>";
        String signout  = "<span class=\"toolbar-link\"><a href=\"/signout\">Sign out</a></span>";
        return front + messages + profile + signout;
        }
        
    }
    
    public static String parseLoggedOutToolbar(String currentPage) {
        String front = "<span class=\"toolbar-link\"><a href=\"/\">Front page</a></span>";
        String messages = "<span class=\"toolbar-link\"><a href=\"/viestit\">All messages</a></span>";
        String signup  = "<span class=\"toolbar-link\"><a href=\"/signup\">Sign up</a></span>";
        String signin  = "<span class=\"toolbar-link\"><a href=\"/login\">Sign in</a></span>";
        
        return front + messages + signup + signin;
    }
}
