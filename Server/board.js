const Sequelize = require('sequelize');

module.exports = class Board extends Sequelize.Model{
    static init(sequelize){
        return super.init({
            bbsTitle: {
                type: Sequelize.STRING(100),
                allowNull: false,
            },
            bbsContent: {
                type: Sequelize.TEXT,
                allowNull: false,
            },
            bbsAvailable:{
                type: Sequelize.BOOLEAN,
                allowNull: false,
            },
            bbsViews:{
                type: Sequelize.INTEGER.UNSIGNED,
                allowNull: true,
            },
            bbsReco:{
                type: Sequelize.INTEGER,
                allowNull: true,
            },
            bbsTag:{
                type: Sequelize.STRING(20),
                allowNull: true,
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
            modelName: 'Board',
            tableName: 'boards',
            charset: 'utf8',
            collate: 'utf8_general_ci',
       });    
    
    }

    
    static associate(db) {
        db.Board.hasMany(db.Comment, { foreignKey: 'commenter', sourceKey: 'id' });
        db.Board.belongsTo(db.User, {foreginKey: 'boarder', targetKey: 'id'});
      }
}