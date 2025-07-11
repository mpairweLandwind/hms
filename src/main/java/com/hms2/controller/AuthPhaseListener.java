// package com.hms2.controller;

// import jakarta.faces.event.PhaseEvent;
// import jakarta.faces.event.PhaseId;
// import jakarta.faces.event.PhaseListener;
// import jakarta.faces.context.FacesContext;
// import jakarta.servlet.http.HttpSession;

// public class AuthPhaseListener implements PhaseListener {
//     @Override
//     public void afterPhase(PhaseEvent event) {
//         FacesContext context = event.getFacesContext();
//         if (context.getViewRoot() == null) return;
//         String viewId = context.getViewRoot().getViewId();

//         // List of public pages
//         boolean isLoginPage = viewId.contains("index.xhtml") || viewId.contains("login.xhtml");

//         HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//         Object user = (session != null) ? session.getAttribute("user") : null;

//         if (!isLoginPage && user == null) {
//             try {
//                 context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/index.xhtml");
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     @Override
//     public void beforePhase(PhaseEvent event) {}
//     @Override
//     public PhaseId getPhaseId() { return PhaseId.RESTORE_VIEW; }
// } 