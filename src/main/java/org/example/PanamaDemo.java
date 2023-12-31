package org.example;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeForeignAccess;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.Scanner;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class PanamaDemo {


    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.load("/usr/lib/x86_64-linux-gnu/libc.so.6");
    }

    public static long SYS_OPEN = 2;
    public static long SYS_WRITE = 1;
    public static int O_APPEND = 02000;
    public static int O_CREATE = 0100;
    public static int O_RDWR = 02;

    public static final MethodHandle openSyscall$MH = openSyscall();

    public static final MethodHandle writeSyscall$MH = writeSyscall();


//mvn clean package && native-image --features=org.example.PanamaFeature --enable-native-access=ALL-UNNAMED --no-fallback --enable-preview -O3 --gc=G1 -jar target/code-fat.jar

    public static void main(String[] args) throws Throwable {

//        System.out.println("wait call start");
//        new Scanner(System.in).next();
        try (Arena memorySession = Arena.ofConfined()) {
            MemorySegment pathName = memorySession.allocateUtf8String("/home/dreamlike/javaCode/code/temp.txt");
            MemorySegment content = memorySession.allocateUtf8String("write string java syscall \n");
            var fd = ((int) openSyscall$MH.invoke(SYS_OPEN, pathName, O_APPEND | O_CREATE | O_RDWR));
            if (fd > 0) {
                System.out.println("writeSyscall$MH will be call");
                new Scanner(System.in).next();
                var writeRes = ((long) writeSyscall$MH.invoke(SYS_WRITE, fd, content, content.byteSize() - 1));
                System.out.println("Fd : " + fd + "writeRes : " + writeRes);
            }
            System.out.println("open fail "+ fd);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }



    public static MethodHandle openSyscall() {
        Linker linker = Linker.nativeLinker();
//        SymbolLookup lookup = linker.defaultLookup();
        SymbolLookup lookup = SymbolLookup.loaderLookup();
        Optional<MemorySegment> memorySegment = lookup.find("syscall");
        MemorySegment syscallAddress = memorySegment.get();
        return linker.downcallHandle(
                syscallAddress,
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    }
// -XX:MaxDirectMemorySize=1024k --enable-preview
    public static MethodHandle writeSyscall() {
        Linker linker = Linker.nativeLinker();
//        SymbolLookup lookup = linker.defaultLookup();
        SymbolLookup lookup = SymbolLookup.loaderLookup();
        Optional<MemorySegment> memorySegment = lookup.find("syscall");
        MemorySegment syscallAddress = memorySegment.get();
        return linker.downcallHandle(
                syscallAddress,
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
        );
    }


}


//
////        MethodHandles.Lookup lookup = MethodHandles.lookup();
////        var handle = lookup.findStatic(Main.class, "args", MethodType.methodType(void.class, int.class, int.class));
////        handle = MethodHandles.insertArguments(handle, handle.type().parameterCount() - 1, 2);
////        handle.invoke(1);
////        MethodHandle trace = MethodHandles.publicLookup().findVirtual(java.io.PrintStream.class,
////                        "println", methodType(void.class, String.class))
////                .bindTo(System.out);
////        MethodHandle cat = MethodHandles.lookup().findVirtual(String.class,
////                "concat", methodType(String.class, String.class));
//////        assertEquals("boojum", (String) cat.invokeExact("boo", "jum"));
////        MethodHandle catTrace = MethodHandles.foldArguments(cat, 1, trace);
////        // also prints "jum":
////        System.out.println((String) catTrace.invokeExact("boo", "jum"));
//////        assertEquals("boojum", (String) catTrace.invokeExact("boo", "jum"));