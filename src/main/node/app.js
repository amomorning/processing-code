const Koa = require('koa');
const path = require('path');
const Router = require('koa-router');
const fs = require('fs');
const { promisify } = require('util');    //将函数promise化
const stat = promisify(fs.stat);    //用来获取文件的信息
const mime = require('mime');   //mime类型获取插件
let app = new Koa();
let router = new Router();
function static(dir) {
    return async (ctx, next) => {
        let pathname = ctx.path;
        let realPath = path.join(dir, pathname);

        console.log("real path " + realPath);
        try {
            let statObj = await stat(realPath);
            if (statObj.isFile()) {
                console.log("Send File !");
                ctx.set('Content-Type', mime.getType(realPath) + ";charset=utf-8");
                ctx.body = fs.createReadStream(realPath)
            } else {
                //如果不是文件，则判断是否存在index.html
                let filename = path.join(realPath, 'index.html')
                console.log("Send index" + filename);
                await stat(filename)
                ctx.set('Content-Type', "text/html;charset=utf-8");
                ctx.body = fs.createReadStream(filename);
            }
        } catch (e) {
            await next();   //交给后面的中间件处理
        }
    }
}
app.use(static(__dirname));
app.use(router.routes());

app.listen(23810);
