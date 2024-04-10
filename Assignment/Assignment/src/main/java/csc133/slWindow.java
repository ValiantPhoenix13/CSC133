
package csc133;
import csc133.Spot.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class slWindow {
    GLFWErrorCallback errorCallback;
    private long window = -1;
    GLFWKeyCallback keyCallback;

    GLFWFramebufferSizeCallback fbCallback;

    int WIN_POS_X = 30, WIN_POX_Y = 90;




    public void slWindow(int win_width, int win_height) {
        glfwSetErrorCallback(errorCallback =
                GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);
        window = glfwCreateWindow(win_width, win_height, "CSC 133", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(window, keyCallback =
                new GLFWKeyCallback() {
                    @Override
                    public void invoke(long window, int key, int scancode, int action, int
                            mods) {
                        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                            glfwSetWindowShouldClose(window, true);
                    }
                });
        glfwSetFramebufferSizeCallback(window, fbCallback =
                new
                        GLFWFramebufferSizeCallback() {
                            @Override
                            public void invoke(long window, int width, int height) {
                                if (width > 0 && height > 0) {
                                    Spot.WIN_WIDTH = width;
                                    Spot.WIN_HEIGHT = height;
                                }
                            }
                        });
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, WIN_POS_X, WIN_POX_Y);
        glfwMakeContextCurrent(window);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(window);
    }
    public long get_oglWindow(int win_width, int win_height){
        if (window == -1){
            Spot.WIN_WIDTH = win_width;
            Spot.WIN_HEIGHT = win_height;
            glfwSetErrorCallback(errorCallback =
                    GLFWErrorCallback.createPrint(System.err));
            if (!glfwInit())
                throw new IllegalStateException("Unable to initialize GLFW");
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_SAMPLES, 8);
            window = glfwCreateWindow(Spot.WIN_WIDTH, Spot.WIN_HEIGHT, "CSC 133", NULL, NULL);
            if (window == NULL)
                throw new RuntimeException("Failed to create the GLFW window");
            glfwSetKeyCallback(window, keyCallback =
                    new GLFWKeyCallback() {
                        @Override
                        public void invoke(long window, int key, int scancode, int action, int
                                mods) {
                            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                                glfwSetWindowShouldClose(window, true);
                        }
                    });
            glfwSetFramebufferSizeCallback(window, fbCallback =
                    new
                            GLFWFramebufferSizeCallback() {
                                @Override
                                public void invoke(long window, int width, int height) {
                                    if (width > 0 && height > 0) {
                                        Spot.WIN_WIDTH = width;
                                        Spot.WIN_HEIGHT = height;
                                    }
                                }
                            });
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, WIN_POS_X, WIN_POX_Y);
            glfwMakeContextCurrent(window);
            int VSYNC_INTERVAL = 1;
            glfwSwapInterval(VSYNC_INTERVAL);
            glfwShowWindow(window);
        }
        return window;
    }
    public void Destroy(){
        glfwDestroyWindow(window);
    }
}
