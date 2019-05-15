package src.kioskfitnessapplication;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class FileCopy extends SimpleFileVisitor<Path> {
    private Path sourceDir;
    private Path targetDir;

    public FileCopy(Path sourceDir, Path targetDir) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
    }

    public static void copy(String sourcePath) throws IOException {
        Path sourceDir = Paths.get(sourcePath);
        Path targetDir = Paths.get(new StringBuilder(System.getProperty("user.home")).append("/Desktop").append("/fitx").toString());
        System.out.println("Start :" + new Date());
        Files.walkFileTree(sourceDir, new FileCopy(sourceDir, targetDir));
        System.out.println("End :" + new Date());
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attributes) {

        try {
            Path targetFile = targetDir.resolve(sourceDir.relativize(file));
            Files.copy(file, targetFile);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
        try {
            Path newDir = targetDir.resolve(sourceDir.relativize(dir));
            Files.createDirectory(newDir);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return FileVisitResult.CONTINUE;
    }

//    public static void main(String[] args) throws IOException {
////        Path sourceDir = Paths.get(args[0]);
////        Path targetDir = Paths.get(args[1]);
//        Path sourceDir = Paths.get("F:/NetBeansProjects/fitx");
//        Path targetDir = Paths.get(new StringBuilder(System.getProperty("user.home")).append("/Desktop").append("/fitx").toString());
//        System.out.println("Start :" + new Date());
//        Files.walkFileTree(sourceDir, new FileCopy(sourceDir, targetDir));
//        System.out.println("End :" + new Date());
//
//    }
}
