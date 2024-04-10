package SlRenderer;

import csc133.slGoLBoardLive;
import csc133.slCamera;
import csc133.Spot;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import slge.slKeyListener;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;


public class slSingleBatchRenderer {
    GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;
    GLFWFramebufferSizeCallback fbCallback;
    int WIN_POS_X = 30, WIN_POX_Y = 90;
    float xmin;
    float ymin;
    float xmax;
    float ymax;
    private static final int OGL_MATRIX_SIZE = 16;
    // call glCreateProgram() here - we have no gl-context here
    int shader_program;
    Matrix4f viewProjMatrix = new Matrix4f();
    FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    int vpMatLocation = 0;
    int renderColorLocation = 0;
    csc133.slWindow WINDOW = new csc133.slWindow();
    long window;
    public void render() throws InterruptedException {
        try {
            WINDOW.slWindow(Spot.WIN_WIDTH, Spot.WIN_HEIGHT);
            window = WINDOW.get_oglWindow(Spot.WIN_WIDTH, Spot.WIN_HEIGHT);
            renderLoop();
            WINDOW.Destroy();
            //keyCallback.free();
            //fbCallback.free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
    void renderLoop() throws InterruptedException {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!glfwWindowShouldClose(window)) {
            glfwWaitEvents();
        }
    } // void renderLoop()
    void initOpenGL() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(0, 0, Spot.WIN_WIDTH, Spot.WIN_HEIGHT);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(color, 1.0f);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
        renderColorLocation = glGetUniformLocation(shader_program, "color");

        return;
    } // void initOpenGL()
    void renderObjects() throws InterruptedException {
        int delay = 500;
        int framerate = 0;
        boolean framerateToggle = false;
        boolean paused = false;
        slGoLBoardLive gameBoard = new slGoLBoardLive(20, 18);
        boolean[][] currentBoard = gameBoard.getLiveCellArray();
        glfwSetKeyCallback(window, slKeyListener::keyCallback);
        while (!glfwWindowShouldClose(window)) {
            try{
                Thread.sleep(delay);
            }catch (java.lang.InterruptedException e){

            }
            if (framerateToggle){
                System.out.println(framerate + " FPS");
            }
            if (slKeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
                System.out.println("The program was gracefully closed.");
                System.exit(0);
            }
            if (slKeyListener.isKeyPressed(GLFW_KEY_D)){
                if (delay == 500){
                    delay = 1000;
                    System.out.println("The board refreshes 500 ms slower");
                } else {
                    delay = delay - 500;
                    System.out.println("The board refreshes at its original speed");
                }

            }
            if (slKeyListener.isKeyPressed(GLFW_KEY_SLASH) && slKeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
                System.out.println("These are all the programmed keys for the board\n\n");
                System.out.println("? ---- Displays a list of key functions");
                System.out.println("ESC ---- Closes the program");
                System.out.println("D ---- Adds a 500 ms delay to the animation");
                System.out.println("F ---- Toggle the frame rate onscreen **UPDATING**");
                System.out.println("H ---- Pauses the board **UPDATING**");
                System.out.println("L ---- Loads a saved state of the board **UNDER DEVELOPMENT**");
                System.out.println("S ---- Saves the current board **UNDER DEVELOPMENT**");
                System.out.println("SPACE ---- Unpauses the board\n\n");
            }
            if (slKeyListener.isKeyPressed(GLFW_KEY_F)){
                if(!framerateToggle){
                    System.out.println("The framerate will now be displayed onscreen");
                    framerateToggle = true;
                }
                if(framerateToggle){
                    System.out.println("The framerate will no longer be displayed");
                    framerateToggle = false;
                }

            }
            if(slKeyListener.isKeyPressed(GLFW_KEY_H)){
                paused = true;
                System.out.println("The board is paused.");
                while (paused){
                    if (slKeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                        System.out.println("The board is unpaused.");
                        paused = false;
                    }
                }
            }
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();
            long ibps = 24;
            float[] vertices = new float [Spot.MAX_ROWS * Spot.MAX_COLS * Spot.VPS * Spot.FPS];
            int[] indices = new int [Spot.MAX_ROWS * Spot.MAX_COLS * Spot.IPS];
            xmin = Spot.WIN_WIDTH/2 - 300;
            xmax = xmin + Spot.length;
            ymax = 800;
            ymin = ymax - Spot.length;
            int index = 0;
            for (int row =0; row < Spot.MAX_ROWS; row++){
                for (int col = 0; col < Spot.MAX_COLS; col++){
                    vertices[index++] = xmin;
                    vertices[index++] = ymin;
                    vertices[index++] = xmax;
                    vertices[index++] = ymin;
                    vertices[index++] = xmax;
                    vertices[index++] = ymax;
                    vertices[index++] = xmin;
                    vertices[index++] = ymax;
                    xmin = xmax + Spot.padding;
                    xmax = xmin + Spot.length;
                }
                xmin = Spot.WIN_WIDTH/2 - 300;
                xmax = xmin + Spot.length;
                ymax = ymin - Spot.padding;
                ymin = ymax - Spot.length;
            }
            int v_indx = 0;
            int my_i = 0;
            while (my_i < indices.length){
                indices[my_i++]= v_indx;
                indices[my_i++]= v_indx+1;
                indices[my_i++]= v_indx+2;
                indices[my_i++]= v_indx;
                indices[my_i++]= v_indx+2;
                indices[my_i++]= v_indx+3;
                v_indx += Spot.VPS;
            }
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
                    createFloatBuffer(vertices.length).
                    put(vertices).flip(), GL_STATIC_DRAW);
            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
                    createIntBuffer(indices.length).
                    put(indices).flip(), GL_STATIC_DRAW);
            glVertexPointer(2, GL_FLOAT, 0, 0L);
            //viewProjMatrix.setOrtho(0, 200, 0, 200, 0, 10);
            slCamera my_cam = new slCamera();
            my_cam.setProjectionOrtho(0, 900, 0, 900, 0 ,5);
            viewProjMatrix = my_cam.getProjectionMatrix();
            glUniformMatrix4fv(vpMatLocation, false,
                    viewProjMatrix.get(myFloatBuffer));
            //glUniform3f(renderColorLocation, 1.0f, 0.498f, 0.153f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            int ci =0;
            for (int r = 0; r < Spot.MAX_ROWS; r++){
                for (int c = 0; c < Spot.MAX_COLS; c++) {
                    if (currentBoard[r][c]) {
                        glUniform3f(renderColorLocation, 0.0f, 1.0f, 0.0f);
                    } else {
                        glUniform3f(renderColorLocation, 1.0f, 0.0f, 0.0f);
                    }
                    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, ibps*ci);
                    ci++;
                }
            }
            gameBoard.updateNextCellArray();
            int VTD = 4320; // need to process 6 Vertices To Draw 2 triangles * 360, the number of triangles
            //glDrawElements(GL_TRIANGLES, VTD, GL_UNSIGNED_INT, 0L);
            glfwSwapBuffers(window);
        }
    } // renderObjects
}

