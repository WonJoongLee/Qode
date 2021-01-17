const Sequelize = require('sequelize');

module.exports = class User extends Sequelize.Model{
    static init(sequelize){
        return super.init({
            userID: {
                type: Sequelize.STRING(20),
                allowNull: false,
                unique: true,
            },
            userPassword: {
                type: Sequelize.STRING(20),
                allowNull: false,
            },
            userName:{
                type: Sequelize.STRING(20),
                allowNull: false,
                unique: true,
            },
            userScore:{
                type: Sequelize.INTEGER.UNSIGNED,
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
            modelName: 'User',
            tableName: 'users',
            charset: 'utf8',
            collate: 'utf8_general_ci',
       });    
    
    }
