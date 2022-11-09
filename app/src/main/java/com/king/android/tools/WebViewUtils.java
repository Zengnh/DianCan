package com.king.android.tools;

import android.webkit.WebView;

public
class WebViewUtils {

    public static void setHtmlCode(String bodyContent, WebView webView) {
        StringBuffer sb = new StringBuffer()
                .append("<html>")
                .append("<head>")
//               .append("<meta name='viewport' content='width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no'/>")
                .append("<style>")
                .append("*{margin:0px;padding:0px;}")
                .append("img{max-width:100%;height:auto}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append(bodyContent)
                .append("</body>")
                .append("</html>");

//        Document parse = Jsoup.parse(sb.toString());
//        Elements img = parse.getElementsByTag("img");
//        for (Element element : img) {
//            String src = element.attr("src");
//            if (src != null && !src.startsWith("http")){
//                element.attr("src", Constants.HOST+src);
//            }
//        }
        webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", null);
    }
}
