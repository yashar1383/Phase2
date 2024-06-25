package model.collision;


import data.Constants;
import model.interfaces.IsCircle;
import model.interfaces.IsPolygon;
import model.objectModel.*;
import model.objectModel.frameModel.FrameModel;
import model.objectModel.projectiles.BulletModel;
import utils.Helper;
import utils.Math;
import utils.Vector;

import java.util.ArrayList;

public class Collision {
    Vector collisionPoint;

    public static boolean IsColliding(ObjectModel a , ObjectModel b){
        if (a instanceof IsPolygon && b instanceof IsPolygon){
            return PolygonSAT((IsPolygon) a ,(IsPolygon) b);
        }
        else if (a instanceof IsCircle && b instanceof IsPolygon){
            return CircleSAT((IsCircle) a, (IsPolygon) b);
        }
        else if (a instanceof IsPolygon && b instanceof IsCircle){
            return CircleSAT((IsCircle) b, (IsPolygon) a);
        }
        else if (a instanceof IsCircle && b instanceof IsCircle){
            return TwoCirclesCheck((IsCircle) a ,(IsCircle) b);
        }
        return false;
    }

    private static boolean TwoCirclesCheck(IsCircle circle1 ,IsCircle circle2) {
        double r1 = circle1.getRadios();
        double r2 = circle2.getRadios();
        Vector position1 = ((ObjectModel)circle1).getPosition();
        Vector position2 = ((ObjectModel)circle2).getPosition();
        if (Math.VectorSize(Math.VectorAdd(position1 ,Math.ScalarInVector(-1 ,position2))) <= r1 + r2){
            return true;
        }
        return false;
    }

    public static void getOutOfFrame(ObjectModel model, FrameModel frame) {
        Vector solution = new Vector();
        for (Vector vector : findTheOuterVertices(frame ,model)){
            solution = Math.VectorAdd(
                    solution,
                    Math.VectorAdd(
                            Math.ScalarInVector(-1 ,model.getPosition()),
                            vector
                    )
            );
        }
        model.setPosition(Math.VectorAdd(model.getPosition() ,solution));
    }

    public void CollisionResponse(ObjectModel a, ObjectModel b) {
        collisionPoint = FindCollisionPoint(a, b);
        if (collisionPoint == null)
            return;
        CollisionHandler collisionHandler = new CollisionHandler(collisionPoint);

        /////epsilon enemy
        if (a instanceof EpsilonModel && b instanceof EnemyModel){
            collisionHandler.EpsilonEnemy((EpsilonModel)a ,(EnemyModel) b);
        }
        else if (b instanceof EpsilonModel && a instanceof EnemyModel){
            collisionHandler.EpsilonEnemy((EpsilonModel)b ,(EnemyModel) a);
        }

        /////enemy enemy
        else if (a instanceof EnemyModel && b instanceof EnemyModel){
            collisionHandler.EnemyEnemy((EnemyModel) a ,(EnemyModel) b);
        }

        /////enemy bullet
        else if (a instanceof EnemyModel && b instanceof BulletModel){
            collisionHandler.EnemyBullet((EnemyModel)a ,(BulletModel)b);
        }
        else if (b instanceof EnemyModel && a instanceof BulletModel){
            collisionHandler.EnemyBullet((EnemyModel)b ,(BulletModel)a);
        }
        /////Collectives
        else if (a instanceof EpsilonModel && b instanceof CollectiveModel){
            collisionHandler.EpsilonCollective((EpsilonModel) a ,(CollectiveModel) b);
        }
        else if (b instanceof EpsilonModel && a instanceof CollectiveModel){
            collisionHandler.EpsilonCollective((EpsilonModel) b ,(CollectiveModel) a);
        }
    }



    private static Vector FindCollisionPoint(ObjectModel a, ObjectModel b) {
        if (a instanceof IsPolygon && b instanceof IsPolygon) {
            IsPolygon polygon = (IsPolygon) a;
            IsPolygon target = (IsPolygon) b;
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < polygon.getVertices().size(); i++) {
                    if (IsInPolygon(target, polygon.getVertices().get(i))) {
                        return polygon.getVertices().get(i);
                    }
                }
                polygon = (IsPolygon) b;
                target = (IsPolygon) a;
            }
        }
        else if ((a instanceof IsCircle && b instanceof IsPolygon) || (a instanceof IsPolygon && b instanceof IsCircle)){
            ObjectModel polygon,circle;
            if (a instanceof IsCircle){
                circle = a;
                polygon = b;
            }
            else {
                circle = b;
                polygon = a;
            }
            Vector direction = Math.VectorWithSize(Math.VectorAdd(Math.ScalarInVector(-1 ,circle.getPosition()) ,polygon.getPosition()) , Constants.EPSILON_DIMENSION.width);
            return Math.VectorAdd(direction ,circle.getPosition());
        }
        else if (a instanceof IsCircle && b instanceof IsCircle){
            double ra = ((IsCircle) a).getRadios();
            double rb = ((IsCircle) a).getRadios();
            Vector v1 = Math.ScalarInVector(rb ,a.getPosition());
            Vector v2 = Math.ScalarInVector(ra ,b.getPosition());
            Vector v3 = Math.VectorAdd(v1 ,v2);
            return Math.VectorWithSize(v3 ,1d / (ra + rb));
        }
        return null;
    }

    private static boolean PolygonSAT(IsPolygon a, IsPolygon b) {
        IsPolygon polygon;
        polygon = a;
        for (int j = 0 ;j < 2 ;j++) {
            for (int i = 0; i < polygon.getVertices().size(); i++) {
                int c = i + 1;
                if (i + 1 == polygon.getVertices().size())
                    c = 0;
                Vector edge = Math.VectorAdd(Math.ScalarInVector(-1, polygon.getVertices().get(i)), polygon.getVertices().get(c));
                Vector normal = Math.NormalWithSize(edge, 1);
                Vector origin = Math.ScalarInVector(0.5, Math.VectorAdd(polygon.getVertices().get(i), polygon.getVertices().get(c)));
                ArrayList<Double> aProj = GivePolygonProj(a, normal, origin);
                ArrayList<Double> bProj = GivePolygonProj(b, normal, origin);
                aProj = Helper.giveMaxMin(aProj);
                bProj = Helper.giveMaxMin(bProj);
                if (CheckGap(aProj, bProj))
                    return false;
            }
            polygon = b;
        }
        return true;
    }

    private static boolean CircleSAT(IsCircle circle ,IsPolygon polygon){
        for (int i = 0 ;i < polygon.getVertices().size() ;i++){
            int c =i + 1;
            if (c == polygon.getVertices().size())
                c = 0;
            Vector normal = Math.NormalWithSize( Math.VectorAdd(Math.ScalarInVector(-1 ,polygon.getVertices().get(i)) ,polygon.getVertices().get(c)),1);
            ArrayList<Double> polygonProj = GivePolygonProj(polygon ,normal ,polygon.getVertices().get(i));
            ArrayList<Double> circleProj = GiveCircleProj(circle ,normal ,polygon.getVertices().get(i));
            polygonProj = Helper.giveMaxMin(polygonProj);
            circleProj = Helper.giveMaxMin(circleProj);
            if (CheckGap(polygonProj, circleProj))
                return false;
        }
        ArrayList<Double> polygonVerticesDistance = new ArrayList<>();
        Vector center = circle.getCenter();
        for (int i = 0 ;i < polygon.getVertices().size() ;i++){
            polygonVerticesDistance.add(Math.VectorSize(Math.VectorAdd(center ,Math.ScalarInVector(-1 ,polygon.getVertices().get(i)))));
        }
        ArrayList<Double> ordered = Helper.giveMaxMin(polygonVerticesDistance);
        int index = polygonVerticesDistance.indexOf(ordered.get(0));

        Vector normal = Math.VectorWithSize(Math.VectorAdd(Math.ScalarInVector(-1 ,center) ,polygon.getVertices().get(index)) ,1);
        ArrayList<Double> polygonProj = GivePolygonProj(polygon ,normal ,center);
        ArrayList<Double> circleProj = GiveCircleProj(circle ,normal ,center);
        polygonProj = Helper.giveMaxMin(polygonProj);
        circleProj = Helper.giveMaxMin(circleProj);
        if (CheckGap(polygonProj, circleProj))
            return false;
        return true;
    }

    private static boolean CheckGap(ArrayList<Double> aMaxMin ,ArrayList<Double> bMaxMin) {
        if ((aMaxMin.get(1) < bMaxMin.get(0)) || (bMaxMin.get(1) < aMaxMin.get(0)))
            return true;
        return false;
    }

    private static ArrayList<Double> GivePolygonProj(IsPolygon polygon , Vector b , Vector origin){
        ArrayList<Double> answer = new ArrayList<>();
        Vector a;
        for (int i = 0 ;i < polygon.getVertices().size() ;i++){
            a = Math.VectorAdd(Math.ScalarInVector(-1 ,origin) ,polygon.getVertices().get(i));
            answer.add(Math.DotProduct(a ,b));
        }
        return answer;
    }

    private static ArrayList<Double> GiveCircleProj(IsCircle circle ,Vector normal ,Vector origin){
        ArrayList<Double> answer = new ArrayList<>();
        Vector position = circle.getCenter();
        double r = circle.getRadios();
        Vector point1 = Math.VectorAdd(position ,Math.VectorWithSize(normal ,r));
        Vector point2 = Math.VectorAdd(position ,Math.VectorWithSize(normal ,-r));
        Vector vector1 = Math.VectorAdd(Math.ScalarInVector(-1 ,origin) ,point1);
        Vector vector2 = Math.VectorAdd(Math.ScalarInVector(-1 ,origin) ,point2);
        answer.add(Math.DotProduct(vector1 ,normal));
        answer.add(Math.DotProduct(vector2 ,normal));
        return answer;
    }

    private static boolean IsInPolygon(IsPolygon polygon ,Vector a){
        int num_vertices = polygon.getVertices().size();
        double x = a.x, y = a.y;
        boolean inside = false;
        Vector p1 = polygon.getVertices().get(0), p2;
        for (int i = 1; i <= num_vertices; i++) {
            p2 = polygon.getVertices().get(i % num_vertices);
            if (y > java.lang.Math.min(p1.y, p2.y)) {
                if (y <= java.lang.Math.max(p1.y, p2.y)) {
                    if (x <= java.lang.Math.max(p1.x, p2.x)) {
                        double x_intersection
                                = (y - p1.y) * (p2.x - p1.x)
                                / (p2.y - p1.y)
                                + p1.x;
                        if (p1.x == p2.x
                                || x <= x_intersection) {
                            inside = !inside;
                        }
                    }
                }
            }
            p1 = p2;
        }
        return inside;
    }

    public static boolean IsInCircle(IsCircle circle ,Vector a){
        double distance = Math.VectorSize(Math.VectorAdd(circle.getCenter() ,Math.ScalarInVector(-1 ,a)));
        if (distance <= circle.getRadios())
            return true;
        return false;
    }

    public static boolean isInFrame(FrameModel frame ,Vector point) {
        double frameX1 = frame.getPosition().x;
        double frameX2 = frame.getPosition().x + frame.getSize().width;
        double frameY1 = frame.getPosition().y;
        double frameY2 = frame.getPosition().y + frame.getSize().height;

        double modelX = point.x;
        double modelY = point.y;

        if (modelX >= frameX1 && modelX <= frameX2 && modelY >= frameY1 && modelY <= frameY2){
            return true;
        }
        return false;
    }


    public static boolean isFullyInFrame(ObjectModel model ,FrameModel frame){
        if (model instanceof IsCircle){
            Vector center = ((IsCircle) model).getCenter();
            double radios = ((IsCircle) model).getRadios();
            Vector top = new Vector(center.x ,center.y - radios);
            Vector right = new Vector(center.x + radios ,center.y);
            Vector bottom = new Vector(center.x ,center.y + radios);
            Vector left = new Vector(center.x - radios ,center.y);
            if (!isInFrame(frame ,top) || !isInFrame(frame ,right) ||
                    !isInFrame(frame ,bottom) || !isInFrame(frame ,left)){
                return false;
            }
        }

        if (model instanceof IsPolygon){
            ArrayList<Vector> vertices = ((IsPolygon) model).getVertices();
            for (Vector vertex : vertices){
                if (!isInFrame(frame ,vertex))
                    return false;
            }
        }

        return true;
    }

    public static ArrayList<Vector> findTheOuterVertices(FrameModel frame ,ObjectModel model){
        ArrayList<Vector> vertices = new ArrayList<>();
        if (model instanceof IsCircle){
            Vector center = ((IsCircle) model).getCenter();
            double radios = ((IsCircle) model).getRadios();
            Vector top = new Vector(center.x ,center.y - radios);
            Vector right = new Vector(center.x + radios ,center.y);
            Vector bottom = new Vector(center.x ,center.y + radios);
            Vector left = new Vector(center.x - radios ,center.y);
            if (isInFrame(frame ,top))
                vertices.add(top);
            if (isInFrame(frame ,right))
                vertices.add(right);
            if (isInFrame(frame ,bottom))
                vertices.add(bottom);
            if (isInFrame(frame ,left))
                vertices.add(left);
        }
        if (model instanceof IsPolygon){
            ArrayList<Vector> polygonVertices = ((IsPolygon) model).getVertices();
            for (Vector vertex : polygonVertices){
                if (!isInFrame(frame ,vertex))
                    vertices.add(vertex);
            }
        }
        return vertices;
    }


}
