import Engine.*;
import Engine.Object;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window =
            new Window
    (1920,1080,"Hello World");
    private ArrayList<Object> objects
            = new ArrayList<>();
    private ArrayList<Object> objectsObj
            = new ArrayList<>();

    private ArrayList<Object> character
            = new ArrayList<>();
    List<Float> temp;


    SkyBoxCube skybox;
    private MouseInput mouseInput;

    Projection projection = new Projection(window.getWidth(),window.getHeight());
    Camera camera = new Camera();
    Camera maincamera = new Camera();
    int cameraToggle = 0;
    public void init() throws IOException {
        window.init();
        GL.createCapabilities();
        mouseInput = window.getMouseInput();
        camera.setPosition(17f, 5f, -17f);
        camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(225f));

        //        skybox = new SkyBoxCube();
        skybox = new SkyBoxCube(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/skybox.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/skybox.vert", GL_VERTEX_SHADER)
                )
        );
        //pijakan
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.98f, 1f, 1f, 1.0f),
                "resources/ground/ground.obj",
                false
        ));
        //gerbang
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 0.276f, 0.0f, 0.00446f),
                "resources/gate/torii.obj",
                true
        ));
        objects.get(1).rotateObject(0.1f, 0.0f, -10f, 0.0f);
        objects.get(0).translateObject(-7f, 0.55f, 10.0f);
        //temple besar
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 0.70f, 0.495f, 1.0f),
                "resources/temple/temple1.obj",
                false
        ));
        objects.get(2).translateObject(-7f, 0.55f, -10.0f);
        //jalan dalam
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 0.215f, 0.215f, 0.00512f),
                "resources/pathway/sidewalk.obj",
                false
        ));
        objects.get(3).translateObject(0.0f, 0.0f, 0.0f);

        //jalan depan
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 0.215f, 0.215f, 0.00512f),
                "resources/pathway/sidewalk2.obj",
                false
        ));

        //karakter naruto
        character.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.878f, 0.674f, 0.411f, 1.0f),
                "resources/naruto/naruto.obj",
                false
        ));
        character.get(0).translateObject(0.0f, 0.0f, 0.0f);

        //pohon sakura
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.803f, 0.596f, 0.749f, 1.0f),
                "resources/pohon sakura/pohonbangku.obj",
                false
        ));
        objects.get(4).translateObject(0.0f, 0.0f, 0.0f);

        //kedai kiri depan
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.349f, 0.239f, 0.2f, 1.0f),
                "resources/village/kedai.obj",
                false
        ));
        objects.get(5).translateObject(0.0f, 0.0f, 0.0f);

        //rumah kecil
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.635f, 0.329f, 0.078f, 1.0f),
                "resources/village/house1.obj",
                false
        ));
        objects.get(6).translateObject(0.0f, 0.0f, 0.0f);

        //temple belakang
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.6470f, 0.0470f, 0.00392f, 1.0f),
                "resources/temple/temple2.obj",
                false
        ));
        objects.get(7).translateObject(0.0f, 0.0f, 0.0f);

        //kolam
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.607f, 0.643f, 0.231f, 1.0f),
                "resources/pond/pond.obj",
                false
        ));
        objects.get(8).translateObject(0.0f, 0.0f, 0.0f);

        //rumah besar
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.635f, 0.329f, 0.078f, 1.0f),
                "resources/village/house2.obj",
                false
        ));
        objects.get(9).translateObject(0.0f, 0.0f, 0.0f);

        //bangunan kecil kiri
        objects.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.760f, 0.337f, 0.243f, 1.0f),
                "resources/temple/shrine.obj",
                false
        ));
        objects.get(10).translateObject(0.0f, 0.0f, 0.0f);
    }

    public void input(){

        temp = objects.get(0).getCenterPoint();
        float move = 0.1f;//kecepatan gerak camera
        float rotateSpeed = 0.03f;
        //fpp
        if (window.isKeyPressed(GLFW_KEY_1)){
            cameraToggle = 1;
        }
        //tpp
        if (window.isKeyPressed(GLFW_KEY_2)){
            cameraToggle = 2;
        }
        //free roam
        if (window.isKeyPressed(GLFW_KEY_3)){
            cameraToggle = 3;
            camera.setPosition(17f, 5f, -17f);
            camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(225f));
        }
        //temple cam
        if (window.isKeyPressed(GLFW_KEY_4)){
            cameraToggle = 4;
        }
        //temple cam
        if (window.isKeyPressed(GLFW_KEY_5)){
            cameraToggle = 5;
        }

        float moveSpeed = 0.04f; // Kecepatan gerakan karakter

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            character.get(0).translateObject(-moveSpeed, 0f, moveSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            character.get(0).translateObject(moveSpeed, 0f, -moveSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            character.get(0).translateObject(moveSpeed, 0f, moveSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            character.get(0).translateObject(-moveSpeed, 0f, -moveSpeed);
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)){
            character.get(0).translateObject(0f, -0.1f, 0.0f);
        }
        if(window.isKeyPressed(GLFW_KEY_SPACE)){
            character.get(0).translateObject(0.0f, 0.1f, 0.0f);
        }
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
            System.out.println(camera.getPosition().get(0)+" "+camera.getPosition().get(1)+" "+camera.getPosition().get(2));
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
            System.out.println(camera.getPosition().get(0)+" "+camera.getPosition().get(1)+" "+camera.getPosition().get(2));
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
            System.out.println(camera.getPosition().get(0)+" "+camera.getPosition().get(1)+" "+camera.getPosition().get(2));
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
            System.out.println(camera.getPosition().get(0)+" "+camera.getPosition().get(1)+" "+camera.getPosition().get(2));
        }
        if (window.isKeyPressed(GLFW_KEY_I)) {
            camera.rotateX(-rotateSpeed);
        }

        // Rotate camera down (Down arrow key)
        if (window.isKeyPressed(GLFW_KEY_K)) {
            camera.rotateX(rotateSpeed);
        }

        // Rotate camera left (Left arrow key)
        if (window.isKeyPressed(GLFW_KEY_J)) {
            camera.rotateY(-rotateSpeed);
        }

        // Rotate camera right (Right arrow key)
        if (window.isKeyPressed(GLFW_KEY_L)) {
            camera.rotateY(rotateSpeed);
        }
        if(mouseInput.isLeftButtonPressed()){
            Vector2f displayVec = window.getMouseInput().getDisplVec();
            camera.addRotation((float)Math.toRadians(displayVec.x * 0.1f),
                    (float)Math.toRadians(displayVec.y * 0.1f));
        }
        if(window.getMouseInput().getScroll().y != 0){
            projection.setFOV(projection.getFOV()- (window.getMouseInput().getScroll().y*0.01f));
            window.getMouseInput().setScroll(new Vector2f());
        }


    }

    public void loop(){

        //
        while (window.isOpen() && !glfwWindowShouldClose(window.getWindowHandle())) {
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL.createCapabilities();
            input();

            //fpp
            if (cameraToggle == 1){
                float rotateSpeed = 0.03f;
                camera.setPosition((float) ((float)character.get(0).getModel().get(3,0)+7.2), (float) ((float)character.get(0).getModel().get(3,1)+2.4), (float) ((float)character.get(0).getModel().get(3,2)-7.2));
                camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(225f));

                //tpp
            }
            if (cameraToggle == 2){
                camera.setPosition((float) ((float)character.get(0).getModel().get(3,0)+11), (float) ((float)character.get(0).getModel().get(3,1)+4), (float) ((float)character.get(0).getModel().get(3,2)-11));
            }
            if (cameraToggle == 4){
                camera.setPosition(-15f, 5f, 15f);
                camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(50f));
            }
            if (cameraToggle == 5){
                camera.setPosition(-15f, 12f, -10f);

            }

            //code
            for (Object object : objects) {
                object.draw(camera, projection);
            }
            for (Object object : character) {
                object.draw(camera, projection);
            }

            skybox.draw(maincamera, projection);
            // Restore state
            glDisableVertexAttribArray(0);
            glfwPollEvents();
        //
        }
    }

    public void run() throws IOException {

        init();
        loop();

        // Terminate GLFW and
        // free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public static void main(String[] args) throws IOException  {
        new Main().run();
    }
}
