// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: user.proto
// Protobuf Java Version: 4.29.0

package org.example.usergrpc.user;

public interface UserOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.example.usergrpc.user.User)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * db에서 할 작업 종류
   * </pre>
   *
   * <code>string category = 1;</code>
   * @return The category.
   */
  java.lang.String getCategory();
  /**
   * <pre>
   * db에서 할 작업 종류
   * </pre>
   *
   * <code>string category = 1;</code>
   * @return The bytes for category.
   */
  com.google.protobuf.ByteString
      getCategoryBytes();

  /**
   * <pre>
   * 아이디
   * </pre>
   *
   * <code>string username = 2;</code>
   * @return The username.
   */
  java.lang.String getUsername();
  /**
   * <pre>
   * 아이디
   * </pre>
   *
   * <code>string username = 2;</code>
   * @return The bytes for username.
   */
  com.google.protobuf.ByteString
      getUsernameBytes();

  /**
   * <pre>
   * 비밀번호
   * </pre>
   *
   * <code>string password = 3;</code>
   * @return The password.
   */
  java.lang.String getPassword();
  /**
   * <pre>
   * 비밀번호
   * </pre>
   *
   * <code>string password = 3;</code>
   * @return The bytes for password.
   */
  com.google.protobuf.ByteString
      getPasswordBytes();
}