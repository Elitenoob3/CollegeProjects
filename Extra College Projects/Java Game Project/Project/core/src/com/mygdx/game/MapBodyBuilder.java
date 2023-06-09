//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mygdx.game;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Iterator;

public class MapBodyBuilder {
    private static float ppt = 0.0F;

    public static Array<Body> buildShapes(Map map, float pixels, World world) {
        ppt = pixels;
        MapObjects objects = map.getLayers().get("Obstacles").getObjects();
        MapObjects levels = map.getLayers().get("Rooms").getObjects();
        MapObjects end = map.getLayers().get("End").getObjects();
        Array<Body> bodies = new Array();

        Filter filter;
        for(Iterator i = objects.iterator(); i.hasNext(); filter = null) {
            MapObject object = (MapObject)i.next();
            filter = new Filter();
            filter.categoryBits = Constants.BIT_WALLS;
            filter.maskBits = (short) (Constants.BIT_PLAYER | Constants.BIT_SCANNER);
            Body body = myCreateBody(object, world, filter);
            bodies.add(body);
        }

        for(Iterator i = levels.iterator(); i.hasNext(); filter = null) {
            MapObject object = (MapObject) i.next();
            filter = new Filter();
            filter.categoryBits = Constants.BIT_LEVEL;
            filter.maskBits = (short) (Constants.BIT_PLAYER);
            Body body = myCreateBody(object, world, filter);
            bodies.add(body);
        }

        for(Iterator i = end.iterator(); i.hasNext(); filter = null) {
            MapObject object = (MapObject)i.next();
            filter = new Filter();
            filter.categoryBits = Constants.BIT_END;
            filter.maskBits = (short) (Constants.BIT_PLAYER);
            Body body = myCreateBody(object, world, filter);
            bodies.add(body);
        }

        return bodies;
    }

    private static Body myCreateBody(MapObject object, World world, Filter filter) {
        if (object == null) {
            return null;
        } else {
            Object shape;
            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            } else {
                if (!(object instanceof CircleMapObject)) {
                    return null;
                }

                shape = getCircle((CircleMapObject)object);
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = world.createBody(bd);
            Fixture fixture = body.createFixture((Shape)shape, 1.0F);
            fixture.setFilterData(filter);
            if(filter.categoryBits == Constants.BIT_LEVEL) fixture.setSensor(true);
            ((Shape)shape).dispose();
            fixture = null;
            return body;
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5F) / ppt, (rectangle.y + rectangle.height * 0.5F) / ppt);
        polygon.setAsBox(rectangle.width * 0.5F / ppt, rectangle.height * 0.5F / ppt, size, 0.0F);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for(int i = 0; i < vertices.length; ++i) {
            //System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
