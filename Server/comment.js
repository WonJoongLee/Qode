const Sequelize = require('sequelize');

module.exports = class Comment extends Sequelize.Model{
    static init(sequelize){
        return super.init({
            userID: {
                type: Sequelize.STRING(20),
                allowNull: false,
                unique: true,
            },
            commentContent: {
                type: Sequelize.TEXT,
                allowNull: false,
            },
            commentReco:{
                type: Sequelize.INTEGER,
                allowNull: false,
            },
            created_at: {
                type: Sequelize.DATE,
                allowNull: false,
                defaultValue: Sequelize.NOW,
            },
        },{
            sequelize,
            timestamps: false,
            underscored: false,
            paranoid: false,     
            modelName: 'comment',
            tableName: 'comments',
            charset: 'utf8',
            collate: 'utf8_general_ci',
       });    
    
    }

    static associate(db) {
        db.Comment.belongsTo(db.Board, {foreginKey: 'commenter', targetKey: 'id'});
      }
}