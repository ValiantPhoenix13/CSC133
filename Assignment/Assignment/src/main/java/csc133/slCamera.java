package csc133;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class slCamera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private float f_left;
    private float f_right;
    private float f_bottom;
    private float f_top;
    private float f_near;
    private float f_far;
    public static Vector3f defaultLookFrom = new Vector3f(0f, 0f, 10f);
    public static Vector3f defaultLookAt = new Vector3f(0f, 0f, -1f);
    public static Vector3f defaultUpVector = new Vector3f(0f, 1f, 0f);
    private Vector3f curLookFrom;
    private Vector3f curLookAt;
    private Vector3f curUpVector;



    private void setCamera(){
        Matrix4f view = null;
        view.identity();
        view = viewMatrix;
        Matrix4f projection = null;
        projection.identity();
        projection = projectionMatrix;
    }
    public slCamera(Vector3f camera_position){
        curLookFrom = camera_position;
        curLookAt = defaultLookAt;
        curUpVector = defaultUpVector;
        viewMatrix = new Matrix4f().identity();
        projectionMatrix = new Matrix4f().identity();
    }
    public slCamera(){
        curLookFrom = defaultLookFrom;
        curLookAt = defaultLookAt;
        curUpVector = defaultUpVector;
        viewMatrix = new Matrix4f().identity();
        projectionMatrix = new Matrix4f().identity();

    }
    public void setProjectionOrtho(){
        projectionMatrix.identity().ortho(f_left, f_right, f_bottom, f_top, f_near, f_far);
    }
    public void setProjectionOrtho(float left, float right, float bottom, float top, float near, float far){
        f_left = left;
        f_right = right;
        f_bottom = bottom;
        f_top = top;
        f_near = near;
        f_far = far;
        projectionMatrix.identity().ortho(f_left, f_right, f_bottom, f_top, f_near, f_far);
    }
    public Matrix4f getViewMatrix(){
        viewMatrix.identity();
        return viewMatrix.lookAt(curLookFrom, curLookAt.add(defaultLookFrom), curUpVector);

    }
    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }
}
