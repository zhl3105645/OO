package com.oocourse.uml1.interact.format;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.OperationParamInformation;
import com.oocourse.uml1.interact.exceptions.user.*;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.models.common.Visibility;

import java.util.List;
import java.util.Map;

/**
 * UML交互接口
 */
@SuppressWarnings("unused")
public interface UmlInteraction {
    /**
     * 获取类数量
     * 指令：CLASS_COUNT
     *
     * @return 类数量
     */
    int getClassCount();

    /**
     * 获取类操作数量
     * 指令：CLASS_OPERATION_COUNT
     *
     * @param className 类名
     * @return 类的操作数量
     * @throws ClassNotFoundException     类未找到异常
     * @throws ClassDuplicatedException   类重复异常
     */
    int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException;

    /**
     * 获取类属性数量
     * 指令：CLASS_ATTR_COUNT
     *
     * @param className 类名
     * @return 类属性操作数量
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
    int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException;

    /**
     * 统计类操作可见性
     * 指令：CLASS_OPERATION_VISIBILITY
     *
     * @param className     类名
     * @param operationName 操作名
     * @return 类操作可见性统计结果
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
    Map<Visibility, Integer> getClassOperationVisibility(String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException;

    /**
     * 获取类属性可见性
     * 指令：CLASS_ATTR_VISIBILITY
     *
     * @param className     类名
     * @param attributeName 属性名
     * @return 属性可见性
     * @throws ClassNotFoundException       类未找到异常
     * @throws ClassDuplicatedException     类重复异常
     * @throws AttributeNotFoundException   属性未找到异常
     * @throws AttributeDuplicatedException 属性重复异常
     */
    Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException;

    /**
     * 获取类属性类型
     * 指令：CLASS_ATTR_TYPE
     *
     * @param className     类名
     * @param attributeName 属性名
     * @return 属性类型
     * @throws ClassNotFoundException       类未找到异常
     * @throws ClassDuplicatedException     类重复异常
     * @throws AttributeNotFoundException   属性未找到异常
     * @throws AttributeDuplicatedException 属性重复异常
     * @throws AttributeWrongTypeException  属性类型错误异常
     */
    String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException;

    /**
     * 统计类操作参数类型
     * 指令：CLASS_OPERATION_PARAM_TYPE
     *
     * @param className     类名
     * @param operationName 操作名
     * @return 类操作参数类型统计结果
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     * @throws MethodWrongTypeException 方法参数类型错误异常
     * @throws MethodDuplicatedException 方法重复异常
     */
    List<OperationParamInformation> getClassOperationParamType(String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException, MethodWrongTypeException, MethodDuplicatedException;

    /**
     * 获取与类相关联的类列表
     * 指令：CLASS_ASSO_CLASS_LIST
     *
     * @param className 类名
     * @return 与类关联的列表
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
    List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException;

    /**
     * 获取顶级父类
     * 指令：CLASS_TOP_BASE
     *
     * @param className 类名
     * @return 顶级父类名
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
    String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException;

    /**
     * 获取实现的接口列表
     * 指令：CLASS_IMPLEMENT_INTERFACE_LIST
     *
     * @param className 类名
     * @return 实现的接口列表
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
    List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException;

    /**
     * 获取类中未隐藏的属性
     * 即违背了面向对象设计中的隐藏信息原则的属性
     * 指令：CLASS_INFO_HIDDEN
     *
     * @param className 类名
     * @return 未隐藏的属性信息列表
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
    List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException;
}
