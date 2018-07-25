package com.company.surveillance.commands;

import com.company.surveillance.managers.CameraManager;
import com.company.surveillancedata.data_calsses.CommandType;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.company.surveillancedata.interfaces.Command;

import java.util.ArrayList;
import java.util.List;



public class StartCameraCommand implements Command {
    private String CLASS_NAME = "StartCameraCommand";
    private ObjectId commandId;
    private CommandType commandType;
    private List<ObjectId> camerasIds;


    public StartCameraCommand(ObjectId commandId, List<ObjectId> camerasIds) {
        String METHOD_NAME = ".StartCameraCommand()";

        this.commandId = commandId;
        if (this.commandId == null) {
            System.out.println(CLASS_NAME + METHOD_NAME + "->COMMAND_ID_IS_NULL");
            this.commandId = new ObjectId();
        }

        this.camerasIds = camerasIds;
        if (this.camerasIds == null) {
            System.out.println(CLASS_NAME + METHOD_NAME + "->CAMERAS_IDS_LIST_IS_NULL");
            this.camerasIds = new ArrayList<>();
        }

        this.commandType = CommandType.START_CAMERA;
    }


    @Override
    public void setCommandId(ObjectId objectId) {
        commandId = objectId;
    }

    @Override
    public ObjectId getCommandId() {
        return commandId;
    }


    @Override
    public void setCommandType(CommandType commandType) {

    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }


    @Override
    public void execute() {
        String METHOD_NAME = ".execute()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        CameraManager cameraManager = CameraManager.getInstance();
        for (int i = 0; i < camerasIds.size(); ++i)
            cameraManager.startCamera(camerasIds.get(i));
    }
}
