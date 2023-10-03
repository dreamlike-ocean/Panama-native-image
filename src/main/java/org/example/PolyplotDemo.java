//package org.example;
//
//import org.graalvm.polyglot.Context;
//import org.graalvm.polyglot.Value;
//
//public class PolyplotDemo {
//    public static void main(String[] args) {
//        try (Context context = Context.newBuilder("js").option("js.ecmascript-version", "2020").build()) {
//            Value jsFunction = context.eval("js", "" +
//                    "(function myJavaScriptFunction(parameter) {         \n" +
//                    "    console.log('[JS] Hello, ' + parameter + '!');  \n" +
//                    "    return parameter.toUpperCase();                 \n" +
//                    "})                                                  \n");
//
//            Value result = jsFunction.execute("JavaScript");
//            if (result.isString()) {
//                System.out.println("[Java] result: " + result.asString());
//            } else {
//                System.out.println("[Java] unexpected result type returned from JavaScript");
//            }
//        }
//
//    }
//}
