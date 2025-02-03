package org.example.usergrpc.user;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 서비스 정의
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: user.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.example.usergrpc.user.User,
      org.example.usergrpc.user.UsergRpcResponse> getUserIdChkAndJoinAndLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "userIdChkAndJoinAndLogin",
      requestType = org.example.usergrpc.user.User.class,
      responseType = org.example.usergrpc.user.UsergRpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.usergrpc.user.User,
      org.example.usergrpc.user.UsergRpcResponse> getUserIdChkAndJoinAndLoginMethod() {
    io.grpc.MethodDescriptor<org.example.usergrpc.user.User, org.example.usergrpc.user.UsergRpcResponse> getUserIdChkAndJoinAndLoginMethod;
    if ((getUserIdChkAndJoinAndLoginMethod = UserServiceGrpc.getUserIdChkAndJoinAndLoginMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getUserIdChkAndJoinAndLoginMethod = UserServiceGrpc.getUserIdChkAndJoinAndLoginMethod) == null) {
          UserServiceGrpc.getUserIdChkAndJoinAndLoginMethod = getUserIdChkAndJoinAndLoginMethod =
              io.grpc.MethodDescriptor.<org.example.usergrpc.user.User, org.example.usergrpc.user.UsergRpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "userIdChkAndJoinAndLogin"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.usergrpc.user.User.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.usergrpc.user.UsergRpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("userIdChkAndJoinAndLogin"))
              .build();
        }
      }
    }
    return getUserIdChkAndJoinAndLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.usergrpc.user.Message,
      org.example.usergrpc.user.Message> getSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendMessage",
      requestType = org.example.usergrpc.user.Message.class,
      responseType = org.example.usergrpc.user.Message.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.usergrpc.user.Message,
      org.example.usergrpc.user.Message> getSendMessageMethod() {
    io.grpc.MethodDescriptor<org.example.usergrpc.user.Message, org.example.usergrpc.user.Message> getSendMessageMethod;
    if ((getSendMessageMethod = UserServiceGrpc.getSendMessageMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getSendMessageMethod = UserServiceGrpc.getSendMessageMethod) == null) {
          UserServiceGrpc.getSendMessageMethod = getSendMessageMethod =
              io.grpc.MethodDescriptor.<org.example.usergrpc.user.Message, org.example.usergrpc.user.Message>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.usergrpc.user.Message.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.usergrpc.user.Message.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("sendMessage"))
              .build();
        }
      }
    }
    return getSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.usergrpc.user.Message,
      org.example.usergrpc.user.SavedMessage> getSavedMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "savedMessage",
      requestType = org.example.usergrpc.user.Message.class,
      responseType = org.example.usergrpc.user.SavedMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.usergrpc.user.Message,
      org.example.usergrpc.user.SavedMessage> getSavedMessageMethod() {
    io.grpc.MethodDescriptor<org.example.usergrpc.user.Message, org.example.usergrpc.user.SavedMessage> getSavedMessageMethod;
    if ((getSavedMessageMethod = UserServiceGrpc.getSavedMessageMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getSavedMessageMethod = UserServiceGrpc.getSavedMessageMethod) == null) {
          UserServiceGrpc.getSavedMessageMethod = getSavedMessageMethod =
              io.grpc.MethodDescriptor.<org.example.usergrpc.user.Message, org.example.usergrpc.user.SavedMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "savedMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.usergrpc.user.Message.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.usergrpc.user.SavedMessage.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("savedMessage"))
              .build();
        }
      }
    }
    return getSavedMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceStub>() {
        @java.lang.Override
        public UserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceStub(channel, callOptions);
        }
      };
    return UserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub>() {
        @java.lang.Override
        public UserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingStub(channel, callOptions);
        }
      };
    return UserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub>() {
        @java.lang.Override
        public UserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceFutureStub(channel, callOptions);
        }
      };
    return UserServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 서비스 정의
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * 아이디 중복검사, 회원가입, 로그인
     * </pre>
     */
    default void userIdChkAndJoinAndLogin(org.example.usergrpc.user.User request,
        io.grpc.stub.StreamObserver<org.example.usergrpc.user.UsergRpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUserIdChkAndJoinAndLoginMethod(), responseObserver);
    }

    /**
     * <pre>
     *메세지 전송 서비스
     * </pre>
     */
    default void sendMessage(org.example.usergrpc.user.Message request,
        io.grpc.stub.StreamObserver<org.example.usergrpc.user.Message> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    /**
     * <pre>
     *저장된 메세지 가지고 오기
     * </pre>
     */
    default void savedMessage(org.example.usergrpc.user.Message request,
        io.grpc.stub.StreamObserver<org.example.usergrpc.user.SavedMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSavedMessageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserService.
   * <pre>
   * 서비스 정의
   * </pre>
   */
  public static abstract class UserServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserService.
   * <pre>
   * 서비스 정의
   * </pre>
   */
  public static final class UserServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UserServiceStub> {
    private UserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 아이디 중복검사, 회원가입, 로그인
     * </pre>
     */
    public void userIdChkAndJoinAndLogin(org.example.usergrpc.user.User request,
        io.grpc.stub.StreamObserver<org.example.usergrpc.user.UsergRpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUserIdChkAndJoinAndLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *메세지 전송 서비스
     * </pre>
     */
    public void sendMessage(org.example.usergrpc.user.Message request,
        io.grpc.stub.StreamObserver<org.example.usergrpc.user.Message> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *저장된 메세지 가지고 오기
     * </pre>
     */
    public void savedMessage(org.example.usergrpc.user.Message request,
        io.grpc.stub.StreamObserver<org.example.usergrpc.user.SavedMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSavedMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserService.
   * <pre>
   * 서비스 정의
   * </pre>
   */
  public static final class UserServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 아이디 중복검사, 회원가입, 로그인
     * </pre>
     */
    public org.example.usergrpc.user.UsergRpcResponse userIdChkAndJoinAndLogin(org.example.usergrpc.user.User request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUserIdChkAndJoinAndLoginMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *메세지 전송 서비스
     * </pre>
     */
    public org.example.usergrpc.user.Message sendMessage(org.example.usergrpc.user.Message request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *저장된 메세지 가지고 오기
     * </pre>
     */
    public org.example.usergrpc.user.SavedMessage savedMessage(org.example.usergrpc.user.Message request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSavedMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserService.
   * <pre>
   * 서비스 정의
   * </pre>
   */
  public static final class UserServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserServiceFutureStub> {
    private UserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 아이디 중복검사, 회원가입, 로그인
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.usergrpc.user.UsergRpcResponse> userIdChkAndJoinAndLogin(
        org.example.usergrpc.user.User request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUserIdChkAndJoinAndLoginMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *메세지 전송 서비스
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.usergrpc.user.Message> sendMessage(
        org.example.usergrpc.user.Message request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *저장된 메세지 가지고 오기
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.usergrpc.user.SavedMessage> savedMessage(
        org.example.usergrpc.user.Message request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSavedMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_USER_ID_CHK_AND_JOIN_AND_LOGIN = 0;
  private static final int METHODID_SEND_MESSAGE = 1;
  private static final int METHODID_SAVED_MESSAGE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_USER_ID_CHK_AND_JOIN_AND_LOGIN:
          serviceImpl.userIdChkAndJoinAndLogin((org.example.usergrpc.user.User) request,
              (io.grpc.stub.StreamObserver<org.example.usergrpc.user.UsergRpcResponse>) responseObserver);
          break;
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((org.example.usergrpc.user.Message) request,
              (io.grpc.stub.StreamObserver<org.example.usergrpc.user.Message>) responseObserver);
          break;
        case METHODID_SAVED_MESSAGE:
          serviceImpl.savedMessage((org.example.usergrpc.user.Message) request,
              (io.grpc.stub.StreamObserver<org.example.usergrpc.user.SavedMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getUserIdChkAndJoinAndLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.usergrpc.user.User,
              org.example.usergrpc.user.UsergRpcResponse>(
                service, METHODID_USER_ID_CHK_AND_JOIN_AND_LOGIN)))
        .addMethod(
          getSendMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.usergrpc.user.Message,
              org.example.usergrpc.user.Message>(
                service, METHODID_SEND_MESSAGE)))
        .addMethod(
          getSavedMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.usergrpc.user.Message,
              org.example.usergrpc.user.SavedMessage>(
                service, METHODID_SAVED_MESSAGE)))
        .build();
  }

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.example.usergrpc.user.UserProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getUserIdChkAndJoinAndLoginMethod())
              .addMethod(getSendMessageMethod())
              .addMethod(getSavedMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
