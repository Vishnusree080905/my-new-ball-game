// Installs the "save to gallery" plugin into the generated android project.
const fs = require('fs'), path = require('path');
if (!fs.existsSync('android')) { console.error('Run "npx cap add android" first.'); process.exit(1); }
const pkg = JSON.parse(fs.readFileSync('capacitor.config.json', 'utf8')).appId;
const dir = path.join('android', 'app', 'src', 'main', 'java', ...pkg.split('.'));
fs.mkdirSync(dir, { recursive: true });
for (const f of ['GallerySaverPlugin.java', 'MainActivity.java'])
  fs.writeFileSync(path.join(dir, f), fs.readFileSync(path.join('native', f), 'utf8').replace(/__PKG__/g, pkg));
console.log('Gallery plugin installed in', dir);
