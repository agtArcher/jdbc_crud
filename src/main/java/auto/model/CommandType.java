package auto.model;

enum CommandType {
    /**
     * insert - for insert commands
     * delete - for delete commands in saved autos list
     * update - for update commands in saved autos list
     * action_temporary - for update and delete operations in unsaved autos list
     */
    INSERT,
    DELETE,
    UPDATE,
    ACTION_TEMPORARY
}
