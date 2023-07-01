package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Object extends ShaderProgram{

    List<Vector3f> vertices;
    int vao;
    int vbo;
    UniformsMap uniformsMap;
    Vector4f color;
    public Matrix4f model;
    List<Object> childObject;
    List<Float> centerPoint;
    float[] lightSwitchDirectionX;
    float[] lightSwitchDirectionZ;
    boolean scene = true;

    public List<Float> getCenterPoint() {
        updateCenterPoint();
        return centerPoint;
    }

    Vector3f[] _pointLightPositions;

    boolean lightObject;

    public Object(List<ShaderModuleData> shaderModuleDataList
            , List<Vector3f> vertices
            , Vector4f color
            , boolean lightObject) {
        super(shaderModuleDataList);
        this.vertices = vertices;
//        setupVAOVBO();
        uniformsMap = new UniformsMap(getProgramId());
        uniformsMap.createUniform(
                "uni_color");
        uniformsMap.createUniform(
                "model");
        uniformsMap.createUniform(
                "projection");
        uniformsMap.createUniform(
                "view");
        uniformsMap.createUniform("dirLight.direction");
        uniformsMap.createUniform("dirLight.ambient");
        uniformsMap.createUniform("dirLight.diffuse");
        uniformsMap.createUniform("dirLight.specular");


        lightSwitchDirectionX = new float[]{1f,-1f,1f,-1f,0,0,0,0,0,0,0,0,0};
        lightSwitchDirectionZ = new float[]{1f,1f,-1f,-1f,0,0,0,0,0,0,0,0,0};
        uniformsMap.createUniform("spotLight.position");
        uniformsMap.createUniform("spotLight.direction");
        uniformsMap.createUniform("spotLight.ambient");
        uniformsMap.createUniform("spotLight.diffuse");
        uniformsMap.createUniform("spotLight.specular");
        uniformsMap.createUniform("spotLight.constant");
        uniformsMap.createUniform("spotLight.linear");
        uniformsMap.createUniform("spotLight.quadratic");
        uniformsMap.createUniform("spotLight.cutOff");
        uniformsMap.createUniform("spotLight.outerCutOff");
        uniformsMap.createUniform("viewPos");
        this.color = color;
        model = new Matrix4f().identity();
        childObject = new ArrayList<>();
        centerPoint = Arrays.asList(0f,0f,0f);
        this.lightObject = lightObject;
    }

    public void setupVAOVBO(){
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
    }

    public void drawSetup(Camera camera, Projection projection){
        bind();
        uniformsMap.setUniform(
                "uni_color", color);
        uniformsMap.setUniform(
                "model", model);
        uniformsMap.setUniform(
                "view", camera.getViewMatrix());
        uniformsMap.setUniform(
                "projection", projection.getProjMatrix());

        _pointLightPositions = new Vector3f[]{
                new Vector3f(5f, 2f, 5f),
                new Vector3f(-5f, 2f, 5f),
                new Vector3f(5f, 2f, -5f),
                new Vector3f(-5f, 2f, -5f),
                new Vector3f(22f, 8f, 10f),
                new Vector3f(-22f, 8f, 10f),
                new Vector3f(22f, 8f, -5f),
                new Vector3f(-22f, 8f, -5f),
                new Vector3f(0f, 8f, 25f),
                new Vector3f(2.8f, 0.85f, -21f),
                new Vector3f(-2.8f, 0.85f, -21f),
                new Vector3f(2.8f, 0.85f, -13f),
                new Vector3f(-10f, 3f, -10f)

        };
        for(int i = 0; i < _pointLightPositions.length; i++){
            uniformsMap.createUniform("pointLight["+i+"].position");
            uniformsMap.createUniform("pointLight["+i+"].ambient");
            uniformsMap.createUniform("pointLight["+i+"].diffuse");
            uniformsMap.createUniform("pointLight["+i+"].specular");
            uniformsMap.createUniform("pointLight["+i+"].constant");
            uniformsMap.createUniform("pointLight["+i+"].linear");
            uniformsMap.createUniform("pointLight["+i+"].quadratic");
        }
        if(!lightObject) {
            uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
            if (scene) {
                uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.1f, 0.1f, 0.1f));
            } else {
                uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.8f, 0.8f, 0.8f));
            }
            uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
            uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));
        } else {
            uniformsMap.setUniform("dirLight.direction", new Vector3f(0f, 0f, 0f));
            uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.6f, 0.6f, 0.6f));
            uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.6f, 0.6f, 0.6f));
            uniformsMap.setUniform("dirLight.specular", new Vector3f(1f, 1f, 1f));
        }

        for (int i = 0; i < _pointLightPositions.length; i++) {
            float dist = (float) Math.sqrt(Math.pow(_pointLightPositions[i].x, 2) + Math.pow(_pointLightPositions[i].z, 2));
            if (dist >= 10000000) {
                lightSwitchDirectionX[i] *= -1000;
                lightSwitchDirectionZ[i] *= -1000;
            }
            _pointLightPositions[i].x += lightSwitchDirectionX[i] * 0.0001f * (100 - dist);
            _pointLightPositions[i].z += lightSwitchDirectionZ[i] * 0.0001f * (100 - dist);


            uniformsMap.setUniform("pointLight[" + i + "].position", _pointLightPositions[i]);
            if (scene) {
                uniformsMap.setUniform("pointLight[" + i + "].ambient", new Vector3f(0.1f, 0.1f, 0.1f));
            } else {
                uniformsMap.setUniform("pointLight[" + i + "].ambient", new Vector3f(0.4f, 0.4f, 0.4f));
            }
            if (i >= 10000000) {
                uniformsMap.setUniform("pointLight[" + i + "].diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
                uniformsMap.setUniform("pointLight[" + i + "].specular", new Vector3f(0.2f, 0.2f, 0.2f));
            } else {
                uniformsMap.setUniform("pointLight[" + i + "].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
                uniformsMap.setUniform("pointLight[" + i + "].specular", new Vector3f(0.5f, 0.5f, 0.5f));
            }
            uniformsMap.setUniform("pointLight[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLight[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLight[" + i + "].quadratic", 0.032f);
        }

        for (int i = 0; i < _pointLightPositions.length; i++) {
            uniformsMap.setUniform("pointLight[" + i + "].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLight[" + i + "].ambient", new Vector3f(0.01f, 0.01f, 0.01f));
            uniformsMap.setUniform("pointLight[" + i + "].diffuse", new Vector3f(0.6f, 0.6f, 0.6f));
            uniformsMap.setUniform("pointLight[" + i + "].specular", new Vector3f(0.0f, 0.0f, 0.0f));
            uniformsMap.setUniform("pointLight[" + i + "].constant", (1f));
            //distance 50
            uniformsMap.setUniform("pointLight[" + i + "].linear", (0.09f));
            uniformsMap.setUniform("pointLight[" + i + "].quadratic", (0.032f));

        }

            uniformsMap.setUniform("pointLight[" + 11 + "].position", _pointLightPositions[11]);
            uniformsMap.setUniform("pointLight[" + 11 + "].ambient", new Vector3f(0.01f, 0.01f, 0.01f));
            uniformsMap.setUniform("pointLight[" + 11 + "].diffuse", new Vector3f(0.6f, 0.6f, 0.6f));
            uniformsMap.setUniform("pointLight[" + 11 + "].specular", new Vector3f(0.0f, 0.0f, 0.0f));
            uniformsMap.setUniform("pointLight[" + 11 + "].constant", (1f));
            //distance 50
            uniformsMap.setUniform("pointLight[" + 11 + "].linear", (0.09f));
            uniformsMap.setUniform("pointLight[" + 11 + "].quadratic", (0.032f));



        // spotLight
        uniformsMap.setUniform("spotLight.position", camera.getPosition());
        uniformsMap.setUniform("spotLight.direction", camera.getDirection());
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.09f);
        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
        uniformsMap.setUniform("spotLight.cutOff", 0f);
        uniformsMap.setUniform("spotLight.outerCutOff", 0f);

        uniformsMap.setUniform("viewPos", camera.getPosition());

        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3,
                GL_FLOAT,
                false,
                0, 0);

    }

    public void draw(Camera camera, Projection projection){
        drawSetup(camera, projection);

        glLineWidth(10); //ketebalan garis
        glPointSize(10); //besar kecil vertex

        glDrawArrays(GL_TRIANGLES,
                0,
                vertices.size());
        for(Object child:childObject){
            child.draw(camera,projection);
        }
    }

    public void translateObject(Float offsetX,Float offsetY,Float offsetZ){
        model = new Matrix4f().translate(offsetX,offsetY,offsetZ).mul(new Matrix4f(model));
        // update center point tak apus buat rotasi di tempat
        for(Object child:childObject){
            child.translateObject(offsetX,offsetY,offsetZ);
        }
    }

    public void rotateObject(Float degree, Float x,Float y,Float z){
        model = new Matrix4f().rotate(degree,x,y,z).mul(new Matrix4f(model));
        // update center point tak apus buat rotasi di tempat
        for(Object child:childObject){
            child.rotateObject(degree,x,y,z);
        }
    }
    public Matrix4f getModel() {
        return model;
    }
    public void updateCenterPoint(){
        Vector3f destTemp = new Vector3f();
        model.transformPosition(0.0f,0.0f,0.0f,destTemp);
        centerPoint.set(0,destTemp.x);
        centerPoint.set(1,destTemp.y);
        centerPoint.set(2,destTemp.z);
    }
    public void scaleObject(Float scaleX,Float scaleY,Float scaleZ){
        model = new Matrix4f().scale(scaleX,scaleY,scaleZ).mul(new Matrix4f(model));
        for(Object child:childObject){
            child.translateObject(scaleX,scaleY,scaleZ);
        }
    }
}

